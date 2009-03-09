package ie.clients.gdma.security;

import static org.junit.Assert.assertNotNull;
import ie.clients.gdma.security.JcifsAuthenticationProvider;
import ie.clients.gdma.util.BaseSpringImplTest;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/testApplicationContext.xml",
        "classpath:/ie/clients/gdma/security/security.xml",
        "classpath:/ie/clients/gdma/dao/hibernate/hibernate-beans.xml" })
public class JcifsAuthenticationProviderTest extends BaseSpringImplTest {

    private static Logger LOG = Logger.getLogger("JcifsAuthenticationProviderTest");

    @Autowired
    private JcifsAuthenticationProvider jcifsAuthenticationProvider;

    @Test
    public void createSelect() {
        assertNotNull(jcifsAuthenticationProvider);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                "rgill", "password1");
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) jcifsAuthenticationProvider
                .authenticate(usernamePasswordAuthenticationToken);
        LOG.debug("user:" + authentication.getPrincipal());
        LOG.debug("auth:" + authentication.getAuthorities());
    }
}
