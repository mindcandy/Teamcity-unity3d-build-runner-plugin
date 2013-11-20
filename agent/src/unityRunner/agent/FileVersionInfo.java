package unityRunner.agent;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;
import java.io.IOException;

public class FileVersionInfo
{
    interface Version extends Library {

        Version INSTANCE = (Version) Native.loadLibrary("Version", Version.class, W32APIOptions.UNICODE_OPTIONS);

        public int GetFileVersionInfoSizeW(String lptstrFilename, int dwDummy);

        public boolean GetFileVersionInfoW(String lptstrFilename, int dwHandle,
                                           int dwLen, Pointer lpData);

        public int VerQueryValueW(Pointer pBlock, String lpSubBlock,
                                  PointerByReference lplpBuffer, IntByReference puLen);

    }

    public static class VS_FIXEDFILEINFO extends com.sun.jna.Structure {
        public int dwSignature;
        public int dwStrucVersion;
        public int dwFileVersionMS;
        public int dwFileVersionLS;
        public int dwProductVersionMS;
        public int dwProductVersionLS;
        public int dwFileFlagsMask;
        public int dwFileFlags;
        public int dwFileOS;
        public int dwFileType;
        public int dwFileSubtype;
        public int dwFileDateMS;
        public int dwFileDateLS;

        public VS_FIXEDFILEINFO(com.sun.jna.Pointer p){
            super(p);
        }
    }

    public static String getLongVersionNumber(String fileName) throws IOException {
        VS_FIXEDFILEINFO infoStructure = getFileInfoStructure(fileName);
        return createLongVersionNumber(infoStructure);
    }

    public static String getShortVersionNumber(String fileName) throws  IOException{
        VS_FIXEDFILEINFO infoStructure = getFileInfoStructure(fileName);
        return createShortVersionNumber(infoStructure);
    }

    public static String createShortVersionNumber(VS_FIXEDFILEINFO info){
        short[] rtnData = getVersionNumberArray(info);
        return ( rtnData[0] + "." + rtnData[1] + "." + rtnData[2]) ;
    }

    public static String createLongVersionNumber(VS_FIXEDFILEINFO info){
        short[] rtnData = getVersionNumberArray(info);
        return ( rtnData[0] + "." + rtnData[1] + "." + rtnData[2] + "." + rtnData[3]) ;
    }

    private static VS_FIXEDFILEINFO getFileInfoStructure(String fileName) throws  IOException{
        int dwDummy = 0;
        int versionLength = Version.INSTANCE.GetFileVersionInfoSizeW(fileName, dwDummy);

        byte[] bufferArray = new byte[versionLength];
        Pointer lpData = new Memory(bufferArray.length);

        PointerByReference lpBuffer = new PointerByReference();
        IntByReference puLen = new IntByReference();
        boolean FileInfoResult = Version.INSTANCE.GetFileVersionInfoW(fileName, 0, versionLength, lpData);
        System.out.println(FileInfoResult);
        int verQueryVal = Version.INSTANCE.VerQueryValueW(lpData,"\\", lpBuffer, puLen);
        VS_FIXEDFILEINFO fileInfoStructure = new VS_FIXEDFILEINFO(lpBuffer.getValue());
        fileInfoStructure.read();

        return fileInfoStructure;
    }

    private static short[] getVersionNumberArray(VS_FIXEDFILEINFO info){
        // Version Number is coded with Major Version X.X each Number in 2 bytes ( dwFileVersionMS )
        // and Minor Version with X.X with also each Number in 2 bytes
        short[] rtnData = new short[4];
        // Get the First Number of the Major by shift out the lower 2 bytes
        rtnData[0] = (short) (info.dwFileVersionMS >> 16);
        // Get the Second Number of the Major by set top 2 bytes to 0
        rtnData[1] = (short) (info.dwFileVersionMS & 0xffff);
        // Same for the Minor Version
        rtnData[2] = (short) (info.dwFileVersionLS >> 16);
        rtnData[3] = (short) (info.dwFileVersionLS & 0xffff);
        return rtnData;
    }
}