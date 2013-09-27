package electricexpansion.common.compatibility;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import dan200.computer.api.ILuaContext;
import dan200.computer.api.ILuaObject;

public class LuaDataInputStream implements ILuaObject
{
    private final byte[]         data;
    private ByteArrayInputStream bs;
    private DataInputStream      ds;
    
    public LuaDataInputStream(byte[] data)
    {
        this.data = data.clone();
        
        bs = new ByteArrayInputStream(this.data);
        ds = new DataInputStream(bs);
    }
    
    @Override
    public String[] getMethodNames()
    {
        return new String[]
        { "readBoolean", "readByte", "readChar", "readDouble", "readFloat", "readInt", "readLong", 
          "readShort", "readString", "readUnsignedByte", "readUnsignedShort" };
    }
    
    @Override
    public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws Exception
    {
        switch (method)
        {
            case 0:
                return new Object[] { ds.readBoolean() };
            case 1:
                return new Object[] { ds.readByte() };
            case 2:
                return new Object[] { ds.readChar() };
            case 3:
                return new Object[] { ds.readDouble() };
            case 4:
                return new Object[] { ds.readFloat() };
            case 5:
                return new Object[] { ds.readInt() };
            case 6:
                return new Object[] { ds.readLong() };
            case 7:
                return new Object[] { ds.readShort() };
            case 8:
                return new Object[] { ds.readUTF() };
            case 9:
                return new Object[] { ds.readUnsignedByte() };
            case 10:
                return new Object[] { ds.readUnsignedShort() };
                
            default:
                return null;
        }
    }
}
