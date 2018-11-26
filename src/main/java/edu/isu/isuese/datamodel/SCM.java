package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("scms")
public class SCM extends Model {

    public String getSCMKey() {
        return getString("scmKey");
    }

    public String getTag() {
        return getString("tag");
    }

    public void setTag(String tag) {
        set("tag", tag);
        save();
    }

    public String getBranch() {
        return getString("branch");
    }

    public void setBranch(String branch) {
        set("branch", branch);
        save();
    }

    public String getURL() {
        return getString("url");
    }

    public void setURL(String url) {
        set("url", url);
        save();
    }

    public SCMType getType() {
        if (get("type") == null) return null;
        else return SCMType.fromValue(getInteger("type"));
    }

    public void setType(SCMType type) {
        if (type == null) set("type", null);
        else set("type", type.value());
        save();
    }
}
