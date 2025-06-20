| Game                              | Format                                                      |
|-----------------------------------|-------------------------------------------------------------|
| Horizon Zero Dawn                 | [PackFile Archive](#packfile-archive)                       |
| Death Stranding                   | [PackFile Archive (Encrypted)](#packfile-archive-encrypted) |
| Horizon Forbidden West (PC)       | [DirectStorage Archive](#directstorage-archive)             |
| Horizon Zero Dawn Remastered (PC) | [DirectStorage Archive](#directstorage-archive)             |

### PackFile Archive

All assets are stored inside archives.

```c
typedef struct {
    uint64 offset;
    uint32 size;
    uint32 key;
} Span <read=Str("[%u .. %u] (%u bytes)", this.offset, this.offset + this.size, this.size)>;

typedef struct {
    uint32 id;
    uint32 key;
    uint64 hash;
    Span span;
} File;

typedef struct {
    Span span;
    Span compressed_span;
} Chunk;

typedef struct {
    uint32 magic; Assert(magic == 0x20304050);
    uint32 key;
    uint64 file_size;
    uint64 data_size;
    uint64 file_entry_count;
    uint32 chunk_entry_count;
    uint32 chunk_entry_size;
} Header;

LittleEndian();

Packfile packfile <open=true>;
File files[file_entry_count];
Chunk chunks[chunk_entry_count];
```

### PackFile Archive (Encrypted)

Uses the same layout as in regular PackFile archives, but has a different magic value:

```diff
@@ -17,7 +17,7 @@ typedef struct {
 } Chunk;
 
 typedef struct {
-    uint32 magic; Assert(magic == 0x21304050);
+    uint32 magic; Assert(magic == 0x20304050);
     uint32 key;
     uint64 file_size;
     uint64 data_size;
```

#### Decryption keys

| Name         | Key                                               |
|--------------|---------------------------------------------------|
| `HEADER_KEY` | `43 94 3A FA 62 AB 1C F4 1C 81 76 F3 3E 9E A8 D2` |
| `DATA_KEY`   | `37 4A 08 6C 95 9D 15 7E E8 F7 5A 3D 3F 7D AA 18` |

#### Decoding the header

Routine for decoding a 32-byte header buffer:

```c
void decrypt_header(ubyte data[32], uint32 key_0, uint32 key_1) {
    ubyte buffer[16];
    ubyte hash[16];

    // Decode the first part of the buffer
    buffer[0:15] = HEADER_KEY;
    buffer[0:3] = key_0;
    data[0:15] = data[0:15] ^ murmurhash3(buffer[0:15], 42);

    // Decode the second part of the buffer
    buffer[0:15] = HEADER_KEY;
    buffer[0:3] = key_1;
    data[16:31] = data[16:31] ^ murmurhash3(buffer[0:15], 42);
}
```

When decoding the `Header`, fields `magic` and `key` are skipped. The `key + 1` is used as the second key:

```c
Header header = ...;
uint32 key_0 = header.key;
uint32 key_1 = header.key + 1; // No tricks here, just increment the key by 1

decrypt_header(&header.file_size, key_0, key_1);
```

Other structures, such as `Chunk` and `File`, are 32 bytes long, so they are processed from the start:

```c
Chunk chunk = ...;
uint32 key_0 = chunk.span.key;
uint32 key_1 = chunk.compressed_span.key;

decrypt_header(&chunk, key_0, key_1);

// Restore keys so we can reuse them later when encrypting the data back
chunk.span.key = key_0;
chunk.compressed_span.key = key_1;
```

#### Decoding the data

Routine for decoding an arbitrary size data buffer:

```c
void decrypt_data(ubyte data[], Span span) {
    ubyte hash[16];

    // Generate a hash for decoding the data buffer
    hash[0:7] = span.offset;
    hash[8:11] = span.size;
    hash[12:15] = span.key;
    hash[0:15] = md5(murmurhash3(hash[0:15], 42));
    
    // Decode the data buffer
    for (uint32 i = 0; i < span.size; i++) {
        data[i] = data[i] ^ hash[i % 16];
    }
}
```

The actual data is decrypted the entire chunk at once:

```c
Chunk chunk = ...;
ubyte data[] = read(offset = chunk.compressed_span.offset, size = chunk.compressed_span.size);

// Decode using the decompressed span
decrypt_data(data, chunk.span);
```

### DirectStorage Archive

Unlike PackFiles, DirectStorage archives contain raw data split into compressed chunks. The contents can't be extracted
without external description or metadata.

> [!NOTE]
> In Horizon Forbidden West, the contents of `.core` and `.core.stream` files is described by the `LocalCacheWinGame\package\streaming_graph.core`
> file stored as a [regular core file](Corefiles#rttibinaryversion-2).

> [!NOTE]
> In Horizon Zero Dawn Remastered, the contents of `.core` and `.stream` files is described by the `LocalCacheDX12\package\PackFileLocators.bin`
> file. For information about its structure, see [this paragraph](#PackFileLocatorsbin).

```c
typedef struct {
    char magic[4]; Assert(magic == "DSAR");
    uint16 version_major; Assert(version_major == 3);
    uint16 version_minor; Assert(version_minor == 1);
    uint32 chunk_count;
    uint32 first_chunk_offset;
    uint64 total_size;
    char   padding[8];
} Header;

typedef enum <ubyte> {
    Compression_LZ4 = 3
} Compression;

typedef struct {
    uint64 offset;
    uint64 compressed_offset;
    uint32 size;
    uint32 compressed_size;
    ubyte  type; // 3 - LZ4
    ubyte  padding[7];
} Chunk;

LittleEndian();
Header header;
Chunk chunks[header.chunk_count];
```

### `PackFileLocators.bin`

> [!NOTE]
> This file is exclusive to Horizon Zero Dawn Remastered

```c
typedef struct {
    uquad Name <format=hex>; // Presumably a MurmurHash3 hash of the name
    uint Offset;
    uint Length;
} PackfileFile <read=Str("%016Lx @ %d (%d bytes)", this.Name, this.Offset, this.Length)>;

typedef struct {
    uint NameLength <hidden=true>;
    char Name[NameLength];
    uint NumFiles <hidden=true>;
    PackfileFile Files [NumFiles];
} Packfile <read=(this.Name)>;

uint NumPackfiles <hidden=true>;
Packfile Packfiles [NumPackfiles] <optimize=false>;
```

### `streaming_links.core`

> [!NOTE]
> This file is exclusive to Horizon Forbidden West

Auxiliary file required and referenced by the `StreamingGraphResource` object from the `streaming_graph.core` file.

Contains a table of pairs of variable-length indices that denote the target group and object index of a link.

Routine for decoding a single link at the given pointer into the `streming_links.bin` data:

```c
void read_link(uint8* in_streaming_links, int32* out_subgroup_index, uint32* out_object_index) {
    uint8 link = *in_streaming_links++;
    if (link & 0x40) {
        // Link inside the current group
        *out_subgroup_index = -1;
        *out_object_index   = read_varint(in_streaming_links, link, 0x3f);
    } else {
        // Link inside a subgroup
        *out_subgroup_index = read_varint(in_streaming_links, link, 0x3f);
        *out_object_index   = read_varint(in_streaming_links, *in_streaming_links++, 0x7f);    
    }
} 

uint32 read_varint(uint8* in_data, uint32 packed, uint32 mask) {
    uint32 value = packed & mask;
    if (packed & 0x80) {
        uint8 temp;
        do {
            temp  = *in_data++;
            value = (value << 7) | (temp & 0x7f);
        } while (temp & 0x80);
    }
    return value;
}
```
