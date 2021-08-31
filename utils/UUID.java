package jdy.zsf.nurserystock.utils;

import java.io.*;
import java.util.*;
import java.security.*;
import java.util.concurrent.*;

public final class UUID implements Serializable, Comparable<UUID>
{
    private static final long serialVersionUID = -1185015143654744140L;
    private final long mostSigBits;
    private final long leastSigBits;
    
    private UUID(final byte[] data) {
        long msb = 0L;
        long lsb = 0L;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; ++i) {
            msb = (msb << 8 | (long)(data[i] & 0xFF));
        }
        for (int i = 8; i < 16; ++i) {
            lsb = (lsb << 8 | (long)(data[i] & 0xFF));
        }
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }
    
    public UUID(final long mostSigBits, final long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }
    
    public static UUID fastUUID() {
        return randomUUID(false);
    }
    
    public static UUID randomUUID() {
        return randomUUID(true);
    }
    
    public static UUID randomUUID(final boolean isSecure) {
        final Random ng = isSecure ? Holder.numberGenerator : getRandom();
        final byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        final byte[] array = randomBytes;
        final int n = 6;
        array[n] &= 0xF;
        final byte[] array2 = randomBytes;
        final int n2 = 6;
        array2[n2] |= 0x40;
        final byte[] array3 = randomBytes;
        final int n3 = 8;
        array3[n3] &= 0x3F;
        final byte[] array4 = randomBytes;
        final int n4 = 8;
        array4[n4] |= (byte)128;
        return new UUID(randomBytes);
    }
    
    public static UUID nameUUIDFromBytes(final byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported");
        }
        final byte[] digest;
        final byte[] md5Bytes = digest = md.digest(name);
        final int n = 6;
        digest[n] &= 0xF;
        final byte[] array = md5Bytes;
        final int n2 = 6;
        array[n2] |= 0x30;
        final byte[] array2 = md5Bytes;
        final int n3 = 8;
        array2[n3] &= 0x3F;
        final byte[] array3 = md5Bytes;
        final int n4 = 8;
        array3[n4] |= (byte)128;
        return new UUID(md5Bytes);
    }
    
    public static UUID fromString(final String name) {
        final String[] components = name.split("-");
        if (components.length != 5) {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        for (int i = 0; i < 5; ++i) {
            components[i] = "0x" + components[i];
        }
        long mostSigBits = Long.decode(components[0]);
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]);
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]);
        long leastSigBits = Long.decode(components[3]);
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]);
        return new UUID(mostSigBits, leastSigBits);
    }
    
    public long getLeastSignificantBits() {
        return this.leastSigBits;
    }
    
    public long getMostSignificantBits() {
        return this.mostSigBits;
    }
    
    public int version() {
        return (int)(this.mostSigBits >> 12 & 0xFL);
    }
    
    public int variant() {
        return (int)(this.leastSigBits >>> (int)(64L - (this.leastSigBits >>> 62)) & this.leastSigBits >> 63);
    }
    
    public long timestamp() throws UnsupportedOperationException {
        this.checkTimeBase();
        return (this.mostSigBits & 0xFFFL) << 48 | (this.mostSigBits >> 16 & 0xFFFFL) << 32 | this.mostSigBits >>> 32;
    }
    
    public int clockSequence() throws UnsupportedOperationException {
        this.checkTimeBase();
        return (int)((this.leastSigBits & 0x3FFF000000000000L) >>> 48);
    }
    
    public long node() throws UnsupportedOperationException {
        this.checkTimeBase();
        return this.leastSigBits & 0xFFFFFFFFFFFFL;
    }
    
    @Override
    public String toString() {
        return this.toString(false);
    }
    
    public String toString(final boolean isSimple) {
        final StringBuilder builder = new StringBuilder(isSimple ? 32 : 36);
        builder.append(digits(this.mostSigBits >> 32, 8));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.mostSigBits >> 16, 4));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.mostSigBits, 4));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.leastSigBits >> 48, 4));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.leastSigBits, 12));
        return builder.toString();
    }
    
    @Override
    public int hashCode() {
        final long hilo = this.mostSigBits ^ this.leastSigBits;
        return (int)(hilo >> 32) ^ (int)hilo;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (null == obj || obj.getClass() != UUID.class) {
            return false;
        }
        final UUID id = (UUID)obj;
        return this.mostSigBits == id.mostSigBits && this.leastSigBits == id.leastSigBits;
    }
    
    @Override
    public int compareTo(final UUID val) {
        return (this.mostSigBits < val.mostSigBits) ? -1 : ((this.mostSigBits > val.mostSigBits) ? 1 : ((this.leastSigBits < val.leastSigBits) ? -1 : ((this.leastSigBits > val.leastSigBits) ? 1 : 0)));
    }
    
    private static String digits(final long val, final int digits) {
        final long hi = 1L << digits * 4;
        return Long.toHexString(hi | (val & hi - 1L)).substring(1);
    }
    
    private void checkTimeBase() {
        if (this.version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
    }
    
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e) {
            try {
                throw new Exception(e);
            }
            catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }
    
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }
    
    private static class Holder
    {
        static final SecureRandom numberGenerator;
        
        static {
            numberGenerator = UUID.getSecureRandom();
        }
    }
}
