package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.IsNull;

import java.util.regex.Matcher;

public class BaseTest {

    Gson gson;

    public BaseTest() {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}
