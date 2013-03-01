package org.joeffice.desktop.file;

import java.io.IOException;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public interface DocumentFileOps<E> {

    public E load() throws IOException;

    public void save();
}
