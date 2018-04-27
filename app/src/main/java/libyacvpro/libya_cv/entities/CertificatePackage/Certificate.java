package libyacvpro.libya_cv.entities.CertificatePackage;

/**
 * Created by Asasna on 9/28/2017.
 */

public class Certificate {
    Integer certificate_id;
    Integer cert_id;
    String cert_name;
    String cert_date;

    public Certificate(Integer certificate_id, Integer cert_id, String cert_name, String cert_date) {
        this.certificate_id = certificate_id;
        this.cert_id = cert_id;
        this.cert_name = cert_name;
        this.cert_date = cert_date;
    }

    public String getCert_date() {
        return cert_date;
    }

    public void setCert_date(String cert_date) {
        this.cert_date = cert_date;
    }

    public Integer getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(Integer certificate_id) {
        this.certificate_id = certificate_id;
    }

    public Integer getCert_id() {
        return cert_id;
    }

    public void setCert_id(Integer cert_id) {
        this.cert_id = cert_id;
    }

    public String getCert_name() {
        return cert_name;
    }

    public void setCert_name(String cert_name) {
        this.cert_name = cert_name;
    }
}
