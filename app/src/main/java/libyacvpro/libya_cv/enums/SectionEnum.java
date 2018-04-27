package libyacvpro.libya_cv.enums;

/**
 * Created by Asasna on 9/28/2017.
 */

public enum SectionEnum {


    INFO('I'), Hobby('H'), EDUCATION('E'), EXPERIENCE('X'),
    LANGUAGE('L'), CERTIFICATE('C'), SPECIALTY('S'), TRAINING('T'), SKILLS('K');

    private Character sectionLetter;
     private SectionEnum(Character s) {
            sectionLetter = s;
         }

        public Character getSectionLetter() {
            return sectionLetter;
        }




}
