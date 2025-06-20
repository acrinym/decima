| Game                               | Format                                                   |
|------------------------------------|----------------------------------------------------------|
| Killzone 2                         | [RTTIBin 1.58](#rttibin-158)                             |
| Killzone 3, Until Dawn Beta        | [RTTIBin 1.73](#rttibin-173)                             |
| Killzone: Mercenary                | [RTTIBin 2.07](#rttibin-207)                             |
| Killzone: Shadow Fall, Until Dawn  | [RTTIBin 2.12](#rttibin-212) [＊](#compressed-core-files) |
| RIGS: Mechanized Combat League     | [RTTIBin 2.19](#rttibin-219) [＊](#compressed-core-files) |
| Horizon Zero Dawn, Death Stranding | [RTTIBinaryVersion 2](#rttibinaryversion-2)              |

> [!NOTE]
> All templates are listed in a format compatible with [010 Editor](https://www.sweetscape.com/010editor/).

### RTTIBin 1.58

```cpp
typedef struct {
    switch (ReadUByte()) {
        case 0x80:
            ubyte pad;
            uint value;
            break;
        case 0x81:
            ubyte pad <hidden=true>;
            ushort value;
            break;
        default:
            ubyte value;
            break;
    }
} varint <read=Str("%d", varint_get(this))>;

uquad varint_get(varint& t) {
    return t.value;
}

typedef struct {
    byte version[15]; Assert(version == "RTTIBin<1.58>  ");
    ubyte endian;

    if (endian) {
        BigEndian();
    } else {
        LittleEndian();
    }

    uint pointer_map_size;
    uint allocation_count;
    uint vram_allocation_count;
    ushort required_bin_count;
    ushort required_vram_bin_count;
} rtti_header;

typedef struct {
    varint count;

    struct {
        ubyte length;
        char name[length];
    } entry[varint_get(count)] <read=Str("%s", this.name), optimize=false>;
} rtti_info;

typedef struct (rtti_info& info) {
    varint count;

    if (varint_get(info.count) > 255) {
        struct {
            ushort value;
        } entry[varint_get(count)] <read=Str("%s", info.entry[value].name)>;
    } else {
        struct {
            ubyte value;
        } entry[varint_get(count)] <read=Str("%s", info.entry[value].name)>;
    }
} rtti_object_types;

typedef struct (rtti_object_types& object_types) {
    struct {
        GUID guid;
        uint size;
        uint unk0;
        uint unk1;
        uint unk2;
    } entry[varint_get(object_types.count)];
} rtti_object_headers;

typedef struct {
    varint count;

    if (varint_get(count)) {
        struct {
            varint size;
            varint alignment;
            varint size2;
            varint size3;
            varint size4;
        } entry[varint_get(count)] <optimize=false>;
    }
} rtti_allocations;

typedef struct (rtti_info& info) {
    if (ReadUByte() == 0) {    
        varint kind;
        varint type <read=Str("%s", info.entry[varint_get(this)].name)>;
        varint count;
    
        struct {
            varint size;
            ubyte data[varint_get(size)];
        } data[varint_get(count)] <optimize=false>;
    } else {
        varint type <read=Str("%s", info.entry[varint_get(this)].name)>;
        varint count;

        struct {
            varint size;
            ubyte data[varint_get(size)];
        } data[varint_get(count)] <optimize=false>;
    }
} rtti_atom_table;

typedef struct (rtti_info& info) {
    varint count;
    rtti_atom_table tables (info) [varint_get(count)] <optimize=false>;
} rtti_atom_tables;

rtti_header header;
rtti_info info;
rtti_object_types object_types (info);
rtti_object_headers object_headers (object_types);
rtti_allocations atom_table_allocations;
rtti_atom_tables atom_tables (info);
varint indirect_object_index;
rtti_allocations object_construction_allocations;
```

### RTTIBin 1.73

```cpp
typedef struct {
    switch (ReadUByte()) {
        case 0x80:
            ubyte pad;
            uint value;
            break;
        case 0x81:
            ubyte pad <hidden=true>;
            ushort value;
            break;
        default:
            ubyte value;
            break;
    }
} varint <read=Str("%d", varint_get(this))>;

uquad varint_get(varint& t) {
    return t.value;
}

typedef struct {
    byte version[15]; Assert(version == "RTTIBin<1.73>  ");
    ubyte endian;

    if (endian) {
        BigEndian();
    } else {
        LittleEndian();
    }

    uint pointer_map_size;
    uint allocation_count;
    uint vram_allocation_count;
    ushort required_bin_count;
    ushort required_vram_bin_count;
} rtti_header;

typedef struct {
    varint count;

    struct {
        ubyte length;
        char name[length];
    } entry[varint_get(count)] <read=Str("%s", this.name), optimize=false>;
} rtti_info;

typedef struct (rtti_info& info) {
    varint count;

    if (varint_get(info.count) > 255) {
        struct {
            ushort value;
        } entry[varint_get(count)] <read=Str("%s", info.entry[value].name)>;
    } else {
        struct {
            ubyte value;
        } entry[varint_get(count)] <read=Str("%s", info.entry[value].name)>;
    }
} rtti_object_types;

typedef struct (rtti_object_types& object_types) {
    varint count; // total explicit objects

    struct {
        GUID guid;
        uint size;
    } entry[varint_get(object_types.count)];
} rtti_object_headers;

typedef struct {
    varint count;

    if (varint_get(count)) {
        struct {
            varint size;
            varint alignment;
            varint size2;
            varint size3;
            varint size4;
        } entry[varint_get(count)] <optimize=false>;
    }
} rtti_allocations;

typedef struct (rtti_info& info) {
    if (ReadUByte() == 0) {    
        varint kind;
        varint type <read=Str("%s", info.entry[varint_get(this)].name)>;
        varint count;
    
        struct {
            varint size;
            ubyte data[varint_get(size)];
        } data[varint_get(count)] <optimize=false>;
    } else {
        varint type <read=Str("%s", info.entry[varint_get(this)].name)>;
        varint count;

        struct {
            varint size;
            ubyte data[varint_get(size)];
        } data[varint_get(count)] <optimize=false>;
    }
} rtti_atom_table;

typedef struct (rtti_info& info) {
    varint count;
    rtti_atom_table tables (info) [varint_get(count)] <optimize=false>;
} rtti_atom_tables;

rtti_header header;
rtti_info info;
rtti_object_types object_types (info);
rtti_object_headers object_headers (object_types);
rtti_allocations atom_table_allocations;
rtti_atom_tables atom_tables (info);
varint indirect_object_index;
rtti_allocations object_construction_allocations;
```

### RTTIBin 2.07

```cpp
typedef struct {
    switch (ReadUByte()) {
        case 0x80:
            ubyte pad;
            uint value;
            break;
        case 0x81:
            ubyte pad <hidden=true>;
            ushort value;
            break;
        default:
            ubyte value;
            break;
    }
} varint <read=Str("%d", varint_get(this))>;

uquad varint_get(varint& t) {
    return t.value;
}

typedef struct {
    byte version[15]; Assert(version == "RTTIBin<2.07>  ");
    ubyte endian;

    if (endian) {
        BigEndian();
    } else {
        LittleEndian();
    }

    uint pointer_map_size;
    uint allocation_count;
    uint vram_allocation_count;
    ushort required_bin_count;
    ushort required_vram_bin_count;
    ubyte unk_20[8];
} rtti_header;

typedef struct {
    varint count;

    struct {
        ubyte length;
        char name[length];
    } entry[varint_get(count)] <read=Str("%s", this.name), optimize=false>;
} rtti_info;

typedef struct (rtti_info& info) {
    varint count;

    if (varint_get(info.count) > 255) {
        struct {
            ushort value;
        } entry[varint_get(count)] <read=Str("%s", info.entry[value].name)>;
    } else {
        struct {
            ubyte value;
        } entry[varint_get(count)] <read=Str("%s", info.entry[value].name)>;
    }
} rtti_object_types;

typedef struct (rtti_object_types& object_types) {
    varint count; // total explicit objects

    struct {
        GUID guid;
        uint size;
    } entry[varint_get(object_types.count)];
} rtti_object_headers;

typedef struct {
    varint count;

    if (varint_get(count)) {
        struct {
            varint size;
            varint alignment;
            varint size2;
            varint size3;
            varint size4;
        } entry[varint_get(count)] <optimize=false>;
    }
} rtti_allocations;

typedef struct (rtti_info& info) {
    if (ReadUByte() == 0) {    
        varint kind;
        varint type <read=Str("%s", info.entry[varint_get(this)].name)>;
        varint count;
    
        struct {
            varint size;
            ubyte data[varint_get(size)];
        } data[varint_get(count)] <optimize=false>;
    } else {
        varint type <read=Str("%s", info.entry[varint_get(this)].name)>;
        varint count;

        struct {
            varint size;
            ubyte data[varint_get(size)];
        } data[varint_get(count)] <optimize=false>;
    }
} rtti_atom_table;

typedef struct (rtti_info& info) {
    varint count;
    rtti_atom_table tables (info) [varint_get(count)] <optimize=false>;
} rtti_atom_tables;

rtti_header header;
rtti_info info;
rtti_object_types object_types (info);
rtti_object_headers object_headers (object_types);
rtti_allocations atom_table_allocations;
rtti_atom_tables atom_tables (info);
varint indirect_object_index;
rtti_allocations object_construction_allocations;
```

### RTTIBin 2.12

```cpp
typedef struct {
    byte version[14]; Assert(version == "RTTIBin<2.12> ");
    ubyte platform; Assert(platform == 3); // PINK
    ubyte endian;

    if (endian) {
        BigEndian();
    } else {
        LittleEndian();
    }

    uint pointer_map_size;
    uint string_table_count;
    uint wide_string_table_count;
    uint asset_count;
} rtti_header;

typedef struct {
    uint count;

    struct {
        uint length;
        char name[length];
        GUID uuid;
    } entry[count] <read=Str("%s", this.name), optimize=false>;
} rtti_info;

typedef struct (rtti_info& info) {
    uint count;
    uint indices[count] <read=Str("%s", info.entry[this].name)>;
} rtti_object_types;

typedef struct (rtti_object_types& object_types) {
    uint count; // total explicit objects

    struct {
        GUID guid;
        uint size;
    } entry[object_types.count];
} rtti_object_headers;

rtti_header header;
rtti_info info;
rtti_object_types object_types (info);
rtti_object_headers object_headers (object_types);
```

### RTTIBin 2.19

```cpp
typedef struct {
    byte version[14]; Assert(version == "RTTIBin<2.19> ");
    ubyte platform; Assert(platform == 3); // PINK
    ubyte endian;

    if (endian) {
        BigEndian();
    } else {
        LittleEndian();
    }

    uint pointer_map_size;
    uint string_table_count;
    uint wide_string_table_count;
    uint asset_count;
} rtti_header;

typedef struct {
    uint count;

    struct {
        uint length;
        char name[length];
        GUID uuid;
    } entry[count] <read=Str("%s", this.name), optimize=false>;
} rtti_info;

typedef struct (rtti_info& info) {
    uint count;
    uint indices[count] <read=Str("%s", info.entry[this].name)>;
} rtti_object_types;

typedef struct (rtti_object_types& object_types) {
    uint count; // total explicit objects

    struct {
        GUID guid;
        uint size;
    } entry[object_types.count];
} rtti_object_headers;

rtti_header header;
rtti_info info;
rtti_object_types object_types (info);
rtti_object_headers object_headers (object_types);
```

### Compressed core files

The actual .core data is also additionally wrapped in a compressed archive:

```cpp
LittleEndian();

uint32 magic; Assert(magic == 0xCB10C183);
uint32 chunk_size;
uint64 output_size;
ubyte checksum[16]; // MurmurHash3 of the decompressed data

local uint32 chunk_index;
local uint32 chunk_count = (output_size + chunk_size - 1) / chunk_size;
uint32 chunk_sizes[chunk_count];

struct {
    for (chunk_index = 0; chunk_index < chunk_count; chunk_index++) {
        struct { ubyte data[chunk_sizes[chunk_index]]; } chunk;
    }
} chunks;
```

Individual chunks are compressed using the LZ4 compression algorithm.

### RTTIBinaryVersion 2

```cpp
LittleEndian();

while (!FEof()) {
    struct {
        uquad type_id;
        uint size;
        ubyte data[size];
    } entry;
}
```
