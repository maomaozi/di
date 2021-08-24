package livedoc.fixtures;

import com.mmaozi.di.ContainedApp;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.fixtures.objects.MyApp;

@FuShengTest
public class AppTest extends BaseFixture {

    private ContainedApp app;

    public void initApp() {
        app = new ContainedApp(MyApp.class);
    }

    public void runApp(String arg) {
        app.run(arg);
    }

    public String getMyAppResult() throws ClassNotFoundException {
        return app.getInstance(MyApp.class).getResult();
    }
}
