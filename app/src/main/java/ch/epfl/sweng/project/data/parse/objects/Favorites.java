package ch.epfl.sweng.project.data.parse.objects;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.project.data.parse.PInterface;
import ch.epfl.sweng.project.data.parse.ParseProxy;
import ch.epfl.sweng.project.util.LogHelper;


@ParseClassName("Favorites")
public final class Favorites extends ParseObject {

    private static final String TAG = "Favorites";
    private Set<String> favorites = new HashSet<>();

    public Favorites() {

    }

    public Favorites(HashSet<String> extFavorites, String idUser) {
        setFavorites(extFavorites);
        setIdUser(idUser);
    }

    public void setFavorites(Set<String> favorites) {
        JSONArray jsonFavorites = new JSONArray(favorites);
        put("favorites", jsonFavorites);
        try {
            save();
        } catch (ParseException e) {
            LogHelper.log(TAG, e.getMessage());
        }
    }

    public String getIdUser() {
        return getString("idUser");
    }

    public void setIdUser(String idUser) {
        put("idUser", idUser);
    }

    public Set<String> getFavoritesFromLocal() {
        return favorites;
    }

    public boolean containsUrl(String url) {
        return favorites.contains(url);
    }

    public void addUrlToLocal(String newUrl) {
        favorites.add(newUrl);
    }

    public void deleteUrlToLocal(String url) {
        favorites.remove(url);
    }

    public Set<String> getFavoritesFromServer() {
        JSONArray urlArray = getJSONArray("favorites");
        Set<String> favoritesSet = new HashSet<>();

        if (urlArray == null) {
            LogHelper.log(TAG, "Error parsing the favoritesList array from JSON");
            return favoritesSet;
        }

        for (int i = 0; i < urlArray.length(); i++) {
            try {
                favoritesSet.add(urlArray.getString(i));
            } catch (JSONException e) {
                LogHelper.log(TAG, e.getMessage());
            }
        }
        return favoritesSet;
    }

    public void synchronizeFromServer() {
        if (ParseProxy.PROXY.internetAvailable()) {
            favorites = getFavoritesFromServer();
        }

    }

    public void synchronizeServer() {
        if (ParseProxy.PROXY.internetAvailable()) {
            PInterface.overrideFavorites(getIdUser(), favorites);
        }
    }
}
