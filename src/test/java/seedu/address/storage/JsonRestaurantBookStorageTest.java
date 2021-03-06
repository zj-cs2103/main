package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalMembers.ALICE;
import static seedu.address.testutil.TypicalMembers.HOON;
import static seedu.address.testutil.TypicalMembers.IDA;
import static seedu.address.testutil.TypicalMembers.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyRestaurantBook;
import seedu.address.model.RestaurantBook;

public class JsonRestaurantBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonRestaurantBookStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readAddressBook(null);
    }

    private java.util.Optional<ReadOnlyRestaurantBook> readAddressBook(String filePath) throws Exception {
        return new JsonRestaurantBookStorage(Paths.get(filePath))
                .readRestaurantBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readAddressBook("notJsonFormatAddressBook.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readAddressBook_invalidMemberAddressBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readAddressBook("invalidMemberAddressBook.json");
    }

    @Test
    public void readAddressBook_invalidAndValidMemberAddressBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readAddressBook("invalidAndValidMemberAddressBook.json");
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempAddressBook.json");
        RestaurantBook original = getTypicalAddressBook();
        JsonRestaurantBookStorage jsonAddressBookStorage = new JsonRestaurantBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveRestaurantBook(original, filePath);
        ReadOnlyRestaurantBook readBack = jsonAddressBookStorage.readRestaurantBook(filePath).get();
        assertEquals(original, new RestaurantBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addMember(HOON);
        original.removeMember(ALICE);
        jsonAddressBookStorage.saveRestaurantBook(original, filePath);
        readBack = jsonAddressBookStorage.readRestaurantBook(filePath).get();
        assertEquals(original, new RestaurantBook(readBack));

        // Save and read without specifying file path
        original.addMember(IDA);
        jsonAddressBookStorage.saveRestaurantBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readRestaurantBook().get(); // file path not specified
        assertEquals(original, new RestaurantBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(null, "SomeFile.json");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyRestaurantBook addressBook, String filePath) {
        try {
            new JsonRestaurantBookStorage(Paths.get(filePath))
                    .saveRestaurantBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(new RestaurantBook(), null);
    }
}
