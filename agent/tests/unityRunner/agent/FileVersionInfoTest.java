package unityRunner.agent;

import com.sun.jna.ptr.PointerByReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class FileVersionInfoTest {
    private FileVersionInfo.VS_FIXEDFILEINFO mFileInfo;

    @Before
    public void setUp() throws Exception {
        // We dont want a filesystem dependent test, and we expect that the Reading File information is
        // tested in the jna Framework - so we just test our Conversion Function by construction a fileInfo Structure

        PointerByReference lpBuffer = new PointerByReference();
        mFileInfo = new FileVersionInfo.VS_FIXEDFILEINFO(lpBuffer.getValue());
        int majorVersion1 = 3;
        int majorVersion2 = 5;
        int minorVersion1 = 1;
        int minorVersion2 = 11234;
        mFileInfo.dwFileVersionMS = ( majorVersion1<<16) +majorVersion2;
        mFileInfo.dwFileVersionLS = ( minorVersion1<<16) +minorVersion2;
    }

    @After
    public void tearDown() throws Exception {
        mFileInfo = null;
    }

    @Test
    public void testGetLongVersionNumber() throws Exception {
        String longVersionNumber = FileVersionInfo.createLongVersionNumber(mFileInfo);
        Assert.assertTrue(longVersionNumber.equals("3.5.1.11234"));
    }

    @Test
    public void testGetShortVersionNumber() throws Exception {
        String shortVersionNumber = FileVersionInfo.createShortVersionNumber(mFileInfo);
        Assert.assertTrue(shortVersionNumber.equals("3.5.1"));
    }

    @Test
    public void testNonExistentFile() {
        boolean exceptionThrown = false;
        try{
            String fileName =  "ThisDoesntExist";
            String version = FileVersionInfo.getLongVersionNumber(fileName);
            System.out.println("This should not happen! Found version for non-existend File: " + fileName +  " version: " + version);
        } catch(Exception e){
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

    }
}
