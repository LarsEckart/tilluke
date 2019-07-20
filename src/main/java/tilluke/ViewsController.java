package tilluke;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/views")
class ViewsController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewsController.class);

    @View("home")
    @Get("/")
    public HttpResponse index() {
        LOG.info("hello");
        return HttpResponse.ok(CollectionUtils.mapOf("loggedIn", true, "username", "sdelamo"));
    }
}
