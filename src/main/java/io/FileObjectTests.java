package io;

import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Klasa File to klasa, która dostarcza informacji na temat pliku.
 * Pozwala także na stworzenie pliku lub katalogu o podanej nazwie.
 * W teście korzystamy z metod:
 * exists()
 * delete()
 * createNewFile()
 * mkdirs(), mkdir()
 *
 */

public class FileObjectTests {

    private String fileName = "someFile";
    private String dirName = "Some/Direct";
    private File file;
    private File directory;

    @Before //before each test
    public void initialize(){

        file = new File(fileName);
        if (file.exists()) file.delete();

        directory = new File(dirName);
        if (directory.exists()) directory.delete();

    }



    @Test
    public void shouldCreateNewFileIfNotExists() throws IOException {

        boolean exists = file.exists();

        assertFalse(exists);

        file.createNewFile();
        exists = file.exists();

        assertTrue(exists);

    }

    @Test
    public void shouldNotCreateNewFileIfAlreadyExists() throws IOException {

        file.createNewFile();
        boolean exists = file.exists();
        assertTrue(exists);

        boolean createdAgain = file.createNewFile();
        assertFalse(createdAgain);
    }

    @Test
    public void shouldCreateNewDirectoryIfNotExists() throws IOException {

        boolean exists = directory.exists();

        assertFalse(exists);

        directory.mkdirs();
        exists = directory.exists();

        assertTrue(exists);

    }

    @Test
    public void shouldNotCreateNewDirectoryIfAlreadyExists() throws IOException {

        directory.mkdirs();
        boolean exists = directory.exists();
        assertTrue(exists);

        boolean createdAgain = directory.createNewFile();
        assertFalse(createdAgain);

       /* File cat = new File("Some");
        for (String f: cat.list()){
            System.out.println(f);
        }*/
    }




}
