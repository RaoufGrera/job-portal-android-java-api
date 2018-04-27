package libyacvpro.libya_cv;

/**
 * Created by Asasna on 9/28/2017.
 */

public class Helper {

    public static  String SectionTitled(Character s) {
        switch (s) {


            case 'I':
                return "معلومة الإضافية";
            case 'H':
                return "الهواية";
            case 'E':
                return "المؤهل العلمي";
            case 'X':
                return "الخبرة";
            case 'C':
                return "الشهادة";
            case 'L':
                return "اللغة";
            case 'S':
                return "التخصص";
            case 'T':
                return "الدورة";
            case 'K':
                return "المهارة";

            default:
                return "خطاء";

        }
    }

}
