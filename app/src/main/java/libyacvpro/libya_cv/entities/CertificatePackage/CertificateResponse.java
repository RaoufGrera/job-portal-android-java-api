package libyacvpro.libya_cv.entities.CertificatePackage;

import java.util.List;

/**
 * Created by Asasna on 9/28/2017.
 */

public class CertificateResponse {
    List<Certificate> certificate;

    public List<Certificate> getCertificate() {
        return certificate;
    }

    public void setCertificate(List<Certificate> certificate) {
        this.certificate = certificate;
    }
}
