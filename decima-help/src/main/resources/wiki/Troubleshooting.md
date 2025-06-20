### Decima Workshop won't start

#### Symptoms

- The console window opens and closes immediately.
- When run from the command line, the `Unrecognized option: --add-opens` error is displayed.

#### Solution

- Ensure you have **Java 17** installed. If you don't, see [Installing Java](#installing-java).
- If you have Java 17 installed, but the issue persists, see [Setting the Java path](#setting-the-java-path).

#### Installing Java

Install Java from the [Adoptium](https://adoptium.net/temurin/releases/?package=jre&arch=x64&version=17) website.
If the issue persists, see [Setting the Java path](#setting-the-java-path).

#### Setting the Java path

You may have multiple Java versions installed on your system. In this case, you need to set the correct Java version as
the default:

1. Press <kbd>Windows key + R</kbd> and paste in `sysdm.cpl`. From there, go to the "Advanced" tab and click on "
   Environment Variables".
2. Under "System variables", find the `Path` variable and click "Edit".
3. Find the path to the Java 17 installation directory and move it to the top of the list.
4. Click "OK" to save the changes.
