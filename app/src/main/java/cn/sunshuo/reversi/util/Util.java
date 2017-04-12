package cn.sunshuo.reversi.util;

/**
 * Created by admin on 2017/3/22
 */

public class Util {

    //拷贝棋盘二维数组，将src[][]中的数组copy到dest[][]数组当中
    public static void copyBinaryArray(byte[][]src,byte[][]dest){
        for(int i=0;i<8;i++)
            System.arraycopy(src[i],0,dest[i],0,8);
    }
}
