package sweet_home.testes;

import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import sweet_home.beans.DataLoader;

public class BaseEJBTest {

    protected static EJBContainer container;
    protected final Logger logger = Logger.getGlobal();

    @BeforeClass
    public static void setUpClass() {
        container = EJBContainer.createEJBContainer();
       
        new DataLoader().carregarDados();
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }
}
