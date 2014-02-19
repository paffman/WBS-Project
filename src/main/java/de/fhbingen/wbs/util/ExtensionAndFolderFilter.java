package de.fhbingen.wbs.util;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
* Filter class that only accepts JPG's.
*/
public class ExtensionAndFolderFilter extends FileFilter {
    /**
     * The accepted extension of this dialog.
     */
    private final String[] acceptedExtensions;

    /**
     * Default constructor.
     * @param ext File extension without "." to accept.
     */
    public ExtensionAndFolderFilter(final String... ext) {
        super();
        this.acceptedExtensions = ext;
    }

    @Override
    public final boolean accept(final File f) {
        String extension = getExtension(f);
        boolean retVal = f.isDirectory();
        for (String s : acceptedExtensions) {
            retVal |= s.equalsIgnoreCase(extension);
        }
        return retVal;
    }

    @Override
    public final String getDescription() {
        return LocalizedStrings.getGeneralStrings().jpgImages();
    }

    /**
     * Gets extension of a file.
     * @param f file to get extension from.
     * @return file extension as string.
     */
    public static String getExtension(final File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
