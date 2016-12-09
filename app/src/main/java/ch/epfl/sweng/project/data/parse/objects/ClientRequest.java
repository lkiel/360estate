package ch.epfl.sweng.project.data.parse.objects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ClientRequest")
public final class ClientRequest extends ParseObject {

    public static final String LOOKFOR_TAG = "propertyId";
    public static final String DESCRIPTION_TAG = "propertyDescription";


    private static final String NAME_TAG = "name";
    private static final String LASTNAME_TAG = "lastName";
    private static final String EMAIL_TAG = "email";
    private static final String PHONE_TAG = "phone";

    public ClientRequest() {
    }

    public void setUser(ParseUser user) {
        setName(user.getString(NAME_TAG), user.getString(LASTNAME_TAG));
        setEmail(user.getEmail());
        setPhone(user.getString(PHONE_TAG));
    }

    private void setName(String name, String lastname) {
        if (name != null && lastname != null) {
            put(NAME_TAG, name);
            put(LASTNAME_TAG, lastname);
        }
    }

    public void setInterestedId(String id) {
        put(LOOKFOR_TAG, id);
    }

    public void setPropertyDescription(String description) {
        put(DESCRIPTION_TAG, description);
    }

    public void setEmail(String email) {
        put(EMAIL_TAG, email);
    }

    public void setPhone(String phone) {
        put(PHONE_TAG, phone);
    }

}
