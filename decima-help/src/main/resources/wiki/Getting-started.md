# Getting started

When you open Decima Workshop for the first time, you will be welcomed with an empty window. The first thing you need to
is to create a new project.

## Project creation

In the main menu, use <kbd>File</kbd> &rArr; <kbd>New</kbd> &rArr; <kbd>Project...</kbd> to open the project creation
dialog:

![](assets/project-creation-dialog.png)

Don't worry, you will understand the purpose of each field in a moment.

|Field|Description|
|-----|-----------|
|Project &rArr; Name|Name of the project presented in the navigator|
|Project &rArr; Type|Which game this project is working with|
|Game &rArr; Executable file|Path to the game's binary executable.<br>For most games, it's the only <kbd>.exe</kbd> file located in the game's root folder|
|Game &rArr; Data directory|Path to the game's archives folder.<br>For most games, it's a folder in the game's root folder that contains a bunch of <kbd>.bin</kbd> files|
|Game &rArr; Compressor library|Path to the compressor library used for compressing/decompressing game data.<br>For most games, it's a file in the game's root folder called <kbd>oo2core_XXX.dll</kbd> [[1]](#Footnotes)|

#### Footnotes
1. You can only use `.dll` files on Windows. Most games on Decima was never released for other OSes but Windows, so you will need to find a version of the library that was _specifically_ compiled for that OS. For Linux, you'll need a `.so` file, and `.dylib` for macOS.

Once you have filled in all the required fields, press the <kbd>OK</kbd> button to continue. On the left, you will
see the project you can double-click on to expand and explore its files.

## Replacing files

In the navigator, right-click on a file you want and select an appropriate action:

- <kbd>Replace Contents...</kbd> to replace file contents with another file from the filesystem
- <kbd>Export Contents...</kbd> to export file contents to the filesystem

Otherwise, you can edit data using [Core Editor](Core-editor).

## Persisting changes

In the main menu, use <kbd>File</kbd> &rArr; <kbd>Repack...</kbd> to open the "Persist Changes" dialog:

![](assets/persist-changes-dialog.png)

Several options are present to control how the changes are persisted:

|Name|Description|
|----|-----------|
|Strategy &rArr; Update changed packfiles|Repacks only the packfiles whose files were changed.<br>Big packfiles might take significant time to repack.|
|Strategy &rArr; Collect changes into a single packfile|Creates a single packfile that contains all changes from modified packfiles.<br>This option cannot be used when changing the same file across different packfiles|
|Options &rArr; Create backup if exists|Creates backup for every modified packfile so they can be restored later|
|Options &rArr; Append if exists|If the selected packfile exists, appends changes rather than truncates it|
|Archive format|The output format of the archive: either encrypted or not; HZD does not support encrypted archives |
|Compression level|Controls how powerful the data compression is.<br>See the description of each compression level for a brief overview|

Once done, press the <kbd>Persist</kbd> button to start the repack process.
