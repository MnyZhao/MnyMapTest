import java.nio.Buffer;

/**
 * Crate by E470PD on 2018/10/26
 */
public class Test {
    public static void main(String[] args) {
        String path1 = "https://uri.amap.com/marker?position=1.2,2.3&name=guangchang&src=mpaage&coordinate=gaode&callnative1";
        String path2 = "https://uri.amap.com/marker?position=1.2,2.3&name=guangchang&src=mpaage&coordinate=gaode&callnative1";
        System.out.println(path1.equals(path2) + "比较结果");
        String name = null;
        System.out.println("Test.main" + (name == null));
        System.out.println(getSplicePath(1.2, 2.3));
        System.out.println(getSplicePathBuffer(1.2, 2.3));
        System.out.println(199+1683+"");
        boolean isopen=true;
        System.out.println("Test.main"+(isopen == true ? 1 : 0));
    }

    private static String getSplicePath(double lon, double lat) {
        String basePath = "https://uri.amap.com/marker?";
        String path = "position=" + lon + "," + lat;
        return basePath + path;
    }

    /**
     * @param lon 经度 必要
     * @param lat 维度 必要
     * @return
     */
    private static String getSplicePathBuffer(double lon, double lat) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://uri.amap.com/marker?");
        buffer.append("position=" + lon + "," + lat);
        return buffer.toString();
    }
}
