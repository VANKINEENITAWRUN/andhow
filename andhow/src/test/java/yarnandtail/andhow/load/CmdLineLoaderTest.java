package yarnandtail.andhow.load;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import yarnandtail.andhow.AndHow;
import yarnandtail.andhow.LoaderProblem;
import yarnandtail.andhow.LoaderValues;
import yarnandtail.andhow.internal.RuntimeDefinition;
import yarnandtail.andhow.name.BasicNamingStrategy;
import yarnandtail.andhow.internal.ValueMapWithContextMutable;
import yarnandtail.andhow.SimpleParams;

/**
 *
 * @author eeverman
 */
public class CmdLineLoaderTest {
	
	RuntimeDefinition appDef;
	ValueMapWithContextMutable appValuesBuilder;

	@Before
	public void init() {
		appValuesBuilder = new ValueMapWithContextMutable();
		
		BasicNamingStrategy bns = new BasicNamingStrategy();
		
		appDef = new RuntimeDefinition();
		appDef.addProperty(SimpleParams.class, SimpleParams.KVP_BOB, bns.buildNames(SimpleParams.KVP_BOB, SimpleParams.class, "KVP_BOB"));
		appDef.addProperty(SimpleParams.class, SimpleParams.KVP_NULL, bns.buildNames(SimpleParams.KVP_NULL, SimpleParams.class, "KVP_NULL"));
		appDef.addProperty(SimpleParams.class, SimpleParams.FLAG_FALSE, bns.buildNames(SimpleParams.FLAG_FALSE, SimpleParams.class, "FLAG_FALSE"));
		appDef.addProperty(SimpleParams.class, SimpleParams.FLAG_TRUE, bns.buildNames(SimpleParams.FLAG_TRUE, SimpleParams.class, "FLAG_TRUE"));
		appDef.addProperty(SimpleParams.class, SimpleParams.FLAG_NULL, bns.buildNames(SimpleParams.FLAG_NULL, SimpleParams.class, "FLAG_NULL"));

	}
	
	@Test
	public void testCmdLineLoaderHappyPath() {
		
		String basePath = SimpleParams.class.getCanonicalName() + ".";
		
		List<String> args = new ArrayList();
		args.add(basePath + "KVP_BOB" + AndHow.KVP_DELIMITER + "test");
		args.add(basePath + "KVP_NULL" + AndHow.KVP_DELIMITER + "not_null");
		args.add(basePath + "FLAG_TRUE" + AndHow.KVP_DELIMITER + "false");
		args.add(basePath + "FLAG_FALSE" + AndHow.KVP_DELIMITER + "true");
		args.add(basePath + "FLAG_NULL" + AndHow.KVP_DELIMITER + "true");
		
		
		CmdLineLoader cll = new CmdLineLoader();
		
		LoaderValues result = cll.load(appDef, args, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasIssues()).count());
		assertEquals("test", result.getExplicitValue(SimpleParams.KVP_BOB));
		assertEquals("not_null", result.getExplicitValue(SimpleParams.KVP_NULL));
		assertEquals(Boolean.FALSE, result.getExplicitValue(SimpleParams.FLAG_TRUE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_FALSE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_NULL));
	}
	

	@Test
	public void testCmdLineLoaderEmptyValues() {
		
		String basePath = SimpleParams.class.getCanonicalName() + ".";
		
		List<String> args = new ArrayList();
		args.add(basePath + "KVP_BOB" + AndHow.KVP_DELIMITER + "");
		args.add(basePath + "KVP_NULL" + AndHow.KVP_DELIMITER + "");
		args.add(basePath + "FLAG_TRUE" + AndHow.KVP_DELIMITER + "");
		args.add(basePath + "FLAG_FALSE" + AndHow.KVP_DELIMITER + "");
		args.add(basePath + "FLAG_NULL" + AndHow.KVP_DELIMITER + "");
		
		CmdLineLoader cll = new CmdLineLoader();
		
		LoaderValues result = cll.load(appDef, args, appValuesBuilder);
		
		assertEquals(0, result.getProblems().size());
		assertEquals(0L, result.getValues().stream().filter(p -> p.hasIssues()).count());
		
		assertNull(result.getExplicitValue(SimpleParams.KVP_BOB));
		assertEquals("bob", result.getEffectiveValue(SimpleParams.KVP_BOB));
		assertNull(result.getExplicitValue(SimpleParams.KVP_NULL));
		assertNull(result.getEffectiveValue(SimpleParams.KVP_NULL));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_TRUE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_FALSE));
		assertEquals(Boolean.TRUE, result.getExplicitValue(SimpleParams.FLAG_NULL));
	}
	
	@Test
	public void testCmdLineLoaderDuplicateValuesAndSpaces() {
		
		String basePath = SimpleParams.class.getCanonicalName() + ".";
		
		List<String> args = new ArrayList();
		args.add(basePath + "KVP_NULL" + AndHow.KVP_DELIMITER + "1");
		args.add(basePath + "KVP_NULL" + AndHow.KVP_DELIMITER + "2");
		args.add(basePath + "KVP_NULL" + AndHow.KVP_DELIMITER + "3");
		args.add(basePath + "FLAG_NULL" + AndHow.KVP_DELIMITER + "true");
		args.add(basePath + "FLAG_NULL" + AndHow.KVP_DELIMITER + "false");
		
		
		CmdLineLoader cll = new CmdLineLoader();
		
		LoaderValues result = cll.load(appDef, args, appValuesBuilder);
		
		assertEquals(3, result.getProblems().size());
		for (LoaderProblem lp : result.getProblems()) {
			assertTrue(lp instanceof LoaderProblem.DuplicatePropertyLoaderProblem);
		}
		
	}

	

}