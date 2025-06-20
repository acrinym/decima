# Command-line interface

Decima Workshop comes with a built-in command-line interface.

To use it, you need to open the terminal application in the root directory
and type the following command to get started:

#### Windows

```bash
decima-cli.exe --help
```

#### Linux

```bash
./bin/decima --help
```

This will show the list of available commands. All following examples will assume the usage of Windows.

### Available commands

| Name (click for usage)                 | Description                                      |
|----------------------------------------|--------------------------------------------------|
| `paths`                                | Dumps all valid file paths                       |
| `entry-points`                         | Dumps all script entry point names and checksums |
| `file-references`                      | Dumps all file references                        |
| [`localization`](#localizing-the-game) | Useful for localization commands                 |
| [`repack`](#repacking)                 | Adds or overwrites files in archives             |
| [`get-oodle`](#getting-oodle)          | Downloads Oodle compression library              |

### Specifying the project

For use with the CLI, you can obtain a project in two ways:

- Create a new project following the instructions [here](Getting-started#project-creation),
  then open its configuration again and grab its identifier from the **UUID** field.
- Supply the path to the game's directory, for example:
    - `C:\Program Files (x86)\Steam\steamapps\common\Horizon Zero Dawn`
    - `E:\SteamLibrary\steamapps\common\DEATH STRANDING DIRECTORS CUT`

Then you can specify the project using the `-p`/`--project` option in most commands:

```bash
decima-cli.exe ... --project "C:\Program Files (x86)\Steam\steamapps\common\Horizon Zero Dawn"
decima-cli.exe ... --project "64e3402d-132d-48ea-ad2b-b7951ca8a754"
```

### Repacking

#### Usage

```bash
decima-cli.exe repack [options] <path to archive to repack> <path to files to add>
```

#### Options

| Names                  | Default | Description                                                                                              |
|------------------------|---------|----------------------------------------------------------------------------------------------------------|
| `-p` or `--project`    |         | [The working project](#specifying-the-project)                                                           |
| `-b` or `--backup`     | `false` | Create a backup of the destination archive if exists                                                     |
| `-e` or `--encrypt`    | `false` | Encrypt the archive (**supported by Death Stranding only**)                                              |
| `-t` or `--truncate`   | `false` | Truncate the archive                                                                                     |
| `-l` or `--level`      | `FAST`  | Compression level                                                                                        |
| `--rebuild-prefetch`   | `true`  | Rebuild prefetch data. Must be used when files have changed in size; otherwise, the game **won't start** |
| `--changed-files-only` | `true`  | Update only changed files in the prefetch. Must be used together with `--rebuild-prefetch`               |

Use the following command to get a complete overview of each option:

```bash
decima-cli.exe repack
```

#### Example

You can utilize CLI's capabilities to repack archives without GUI interactions easily.

Let's say you want to repack the following files from Death Stranding:

- `ds/artparts/characters/sam_sam/part_sam_body_naked.core`
- `ds/artparts/characters/sam_sam/part_sam_body_private.core`

You will need to prepare a folder with the hierarchical structure of files that you need to add to the archive:

```
<source directory>
   \--ds
      \--artparts
         \--characters
            \--sam_sam
                part_sam_body_naked.core
                part_sam_body_private.core
```

When everything is ready, you can execute the following command in the terminal:

```bash
decima-cli.exe repack --backup --project <project> path/to/archive.bin <source directory>
```

> [!NOTE]
> It's always recommended to repack the "patch" archive because of how the game loads resources.
>
> - For **Death Stranding**, it's `<game directory>/data/59b95a781c9170b0d13773766e27ad90.bin`
>
> - For **Horizon Zero Dawn**, it's `<game directory>/Packed_DX12/Patch.bin`, but you can create your own archive
    called `Patch_YourModName.bin`.

### Getting Oodle

#### Prerequisites

1. Follow the provided instructions to access the Unreal Engine repository: https://github.com/EpicGames/Signup
2. Download the following file: https://github.com/EpicGames/UnrealEngine/blob/release/Engine/Build/Commit.gitdeps.xml

#### Usage

```bash
decima-cli.exe get-oodle get-oodle [options] <path to Commit.gitdeps.xml>
```

#### Options

| Names                | Description                                                              | Default                   |
|----------------------|--------------------------------------------------------------------------|---------------------------|
| `-o` or `--output`   | Output directory                                                         | Current working directory |
| `-d` or `--default`  | Use default choice or prompt if multiple files were found                | `true`                    |
| `-p` or `--platform` | The target platform. Valid values: `WINDOWS_64`, `LINUX_64`, `DARWIN_64` | `WINDOWS_64`              |

### Localizing the game

Decima Workshop will extract localization resources as a JSON file that can be edited using any text editor.

#### Workflow

To start localizing the game, you first need to understand the workflow:

1. Choose the source and target languages
2. Extract the localization resources from the game
3. Translate the extracted resources
4. Compile the translated resources
5. Pack the compiled resources back into the game

The source language is used as a reference for the localization. The target language is the language you want to edit.

> [!NOTE]
> You can't add a new language to the game; you can only translate the existing ones.

<details>
<summary>Languages supported by Horizon Zero Dawn</summary>

- `Arabic`
- `Chinese_Simplified`
- `Chinese_Traditional`
- `Danish`
- `Dutch`
- `English`
- `Finnish`
- `French`
- `German`
- `Italian`
- `Japanese`
- `Korean`
- `LATAMPOR`
- `LATAMSP`
- `Norwegian`
- `Polish`
- `Portugese` (not a typo)
- `Russian`
- `Spanish`
- `Swedish`
- `Turkish`

</details>

<details>
<summary>Languages supported by Death Stranding</summary>

- `Arabic`
- `Chinese_Simplified`
- `Chinese_Traditional`
- `Czech`
- `Danish`
- `Dutch`
- `English`
- `English_UK`
- `Finnish`
- `French`
- `German`
- `Greek`
- `Hungarian`
- `Italian`
- `Japanese`
- `Korean`
- `LATAMPOR`
- `LATAMSP`
- `Norwegian`
- `Polish`
- `Portuguese`
- `Russian`
- `Spanish`
- `Swedish`
- `Turkish`

</details>

#### Usage

To extract the localization resources, use the following command:

```bash
decima localization export --project <project> --output <output file>.json --source <source language> --target <target language>
```

To compile the localized resources, use the following command:

```bash
decima localization import --project <project> --input <output>.json --output <directory for compiled files>
```

To pack the compiled resources back into the game, use the following command:

```bash
decima repack --backup --project <project> <target archive>.bin <directory for compiled files>
```

#### Localization format

The format is simple and easy to understand:

```json
{
  "source": "English",
  "target": "Japanese",
  "files": {
    "localized/sentences/ds_lines_cutscene/sq_cs00_s00100/sentences.core": {
      "3e87837d-bb1c-d746-9048-87ff70e79d1b": {
        "source": "Once, there was an explosion...",
        "target": "昔 爆発があった",
        "voice": {
          "gender": "Male",
          "name": "Sam"
        },
        "show": "auto"
      }
    }
  }
}
```

> [!IMPORTANT]
> You must not change the top-level `source` and `target` fields. They are used to identify the source and target
> languages specified during the export by the tool to merge changed lines into the game files correctly.

You are interested in objects that have a sequence of random characters as their key.

- The `source` field contains the original text that can be used as a reference. You should **not** edit this field.
- The `target` field contains the translated text. This is the field you will be editing.
- The `voice` field is an optional and may not be present for every line. Contains information about the speaker and
  their gender. Changes don't have any effect.
- The `show` field is used to control the visibility of the subtitle in the game. Valid values are:
-
    - `always` - the subtitle will always be shown even if subtitles are turned off in the game settings
-
    - `never` - the subtitle will never be shown even if subtitles are turned on in the game settings
-
    - `auto` - the subtitle will be shown only if subtitles are turned on in the game settings

> [!NOTE]
> You might want to enforce subtitles to be shown for in-game text written in a foreign language. And vice versa, hide
> subtitles for in-game text written in the same language.

#### Example

Let's say you want to correct Death Stranding's localization of several lines in English using the Japanese
localization as a reference:

```bash
decima localization export --project <project> --output localization.json --source English --target Japanese
... edit localization.json ...
decima localization import --project <project> --input localization.json --output compiled
decima repack --backup --project <project> "<game directory>/data/59b95a781c9170b0d13773766e27ad90.bin" compiled
```
