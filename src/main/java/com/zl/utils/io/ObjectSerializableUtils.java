package com.zl.utils.io;

import java.io.*;

/**
 * @className: com.craw.nd.util.io-> FileSerializable
 * @description: 对象序列化
 * @author: zl
 * @createDate: 2023-02-14 16:21
 * @version: 1.0
 * @todo:
 */
public class ObjectSerializableUtils<T> {
    public void serializable(String filePath, T t) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(t);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Object deserialization(String filePath) {
        T t = null;
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            t = (T) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();

        } catch (ClassNotFoundException c) {
            c.printStackTrace();

        }
        return t;
    }
}
