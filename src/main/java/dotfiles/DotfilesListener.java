package dotfiles;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class DotfilesListener implements BulkFileListener {
    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {
        for (VFileEvent event : events) {
            VirtualFile file = event.getFile();
            if (file == null) {
                continue;
            }
            for (Dotfile dotfile : Dotfile.getDotfiles()) {
                if (Objects.equals(dotfile.getPath(), file.getPath())) {
                    dotfile.apply();
                }
            }
        }
    }
}
