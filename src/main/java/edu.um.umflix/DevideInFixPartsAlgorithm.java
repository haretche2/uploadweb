package edu.um.umflix;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DevideInFixPartsAlgorithm implements DivideAlgorithm{
    public static final int NUMEROFPARTS = 3;

    @Override
    public List<Byte[]> devide(byte[] bytes) {
        //First, create the arrays, and divide them into one third of the larger array.
        //Notice that remainder is added to the third array
        Byte[] newArray1 = new Byte [bytes.length / NUMEROFPARTS];
        Byte[] newArray2 = new Byte [bytes.length / NUMEROFPARTS];
        Byte[] newArray3 = new Byte[bytes.length / NUMEROFPARTS + bytes.length % NUMEROFPARTS];

        //Next, you must run a for loop that copies everything out of the first array into the next ones

        for (int i = 0; i < bytes.length; i++)
        {
            //it will check to see if it is the first, second, or last thirds of first array
            if (i < newArray1.length)
                newArray1[i] = bytes[i];
            else if (i < newArray1.length + newArray2.length)
                newArray2[i-newArray1.length] = bytes[i];
            else
                newArray3[i-newArray1.length-newArray2.length] = bytes[i];
        }
        List<Byte[]> arrays = new ArrayList<Byte[]>();
        arrays.add(newArray1);
        arrays.add(newArray2);
        arrays.add(newArray3);

        return arrays;
    }
}
