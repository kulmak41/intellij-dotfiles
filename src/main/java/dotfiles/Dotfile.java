package dotfiles;

import java.io.File;
import java.util.List;

public abstract class Dotfile {
    protected final static String directory = System.getProperty("user.home") + File.separator + ".idea";
    private static List<Dotfile> dotfiles;

    public abstract String getPath();

    public abstract void apply();

    public static String getDirectory() {
        return directory;
    }

    public static List<Dotfile> getDotfiles() {
        if (dotfiles == null) {
            dotfiles = List.of(AppearanceDotfile.getInstance(), KeymapDotfile.getInstance());
        }
        return dotfiles;
    }
}
