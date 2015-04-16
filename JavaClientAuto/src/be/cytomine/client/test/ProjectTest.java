package be.cytomine.client.test;

import be.cytomine.client.CytomineException;
import be.cytomine.client.Project;
import be.cytomine.client.Server;
import be.cytomine.client.abst.AbstractDomain;
import be.cytomine.client.abst.AbstractProject;
import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lrollus on 4/6/14.
 */
public class ProjectTest extends TestCase{

    JSONObject json57;
    JSONObject json110;
    Server server;
    String date;

    static long ID_PROJECT = 401l;

    static long ID_USER1 = 101l;

    static long ID_ONTOLOGY = 301;

    @Before
    public void setUp() throws Exception {
         server = new Server();
        JSONParser parser=new JSONParser();
        json57 =(JSONObject)parser.parse(new FileReader("testdata/project/project_57.json"));
        json110 =(JSONObject)parser.parse(new FileReader("testdata/project/project_110.json"));
        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBuildFromParams() throws Exception {
        Project project = new Project();
        project.build("TEST",63l,false,true,new ArrayList(), new ArrayList());
        assertEquals("TEST", project.getName());
        assertEquals(null,project.getId());
        assertEquals(new Boolean(false),project.getRetrievalDisable());
        assertEquals(new Boolean(true),project.getRetrievalAllOntology());
    }

    @Test
    public void testBuildFromJSON() throws Exception {
        Project project = new Project();
        project.build(json57);
        assertEquals("ULG-LBTD-NEO04",project.getName());
        assertEquals(new Long(57l),project.getId());
        assertEquals(new Boolean(false),project.getIsReadOnly());
    }

    @Test
    public void testToJSON() throws Exception {
        Project project = new Project();
        project.build(json110);
        JSONObject json = project.toJSON();
        assertEquals(json110.get("name"),json.get("name"));

        //check if all JSON fields are there and equals
        Iterator it = json110.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            assertEquals(pairs.getKey()+" check if present",true,json.containsKey(pairs.getKey()));
            assertEquals(pairs.getKey()+" check if equals",pairs.getValue(),json.get(pairs.getKey()));
        }

        //check if not other JSON fields are there
        it = json.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            assertEquals(pairs.getKey()+" check if present",true,json110.containsKey(pairs.getKey()));
            assertEquals(pairs.getKey()+" check if equals",pairs.getValue(),json110.get(pairs.getKey()));
        }
    }

    @Test
    public void testGet() throws Exception {
        Project project = Project.get(server,ID_PROJECT);
        assertEquals("project",project.getName());
        assertEquals(new Long(ID_PROJECT),project.getId());

        try {
            Project.get(server,-1l);
            fail();
        } catch(CytomineException e) {
            assertEquals(404,e.code);
        }
    }

    @Test
    public void testAdd() throws Exception {
        Project project = new Project();

        List admins = new ArrayList();
        admins.add(ID_USER1);

        List users = new ArrayList();
        users.add(ID_USER1);

        project.build("TESTADD-"+date,ID_ONTOLOGY,false,true,admins,users);

        assertNull(project.getId());
        project.add(server);
        assertNotNull(project.getId());

        Project aBadProject = new Project();
        try {
            aBadProject.build("",-1l,false,true,admins,users);
            aBadProject.add(server);
            fail();
        } catch(CytomineException e) {
            assertFalse(e.code+" should be 4xx", e.code == 200);
        }
         
    }


    @Test
    public void testEdit() throws Exception {
        Project project = new Project();
        project.build("TESTEDIT-"+date,ID_ONTOLOGY,false,true,new ArrayList(),new ArrayList());
        project.add(server);
        assertNotNull(project.getId());

        project.setName("TESTEDIT-EDITED-"+date);
        project.edit(server);
        assertNotNull(project.getId());
        assertEquals("TESTEDIT-EDITED-"+date,project.getName());

        try {
            project.setName("");
            project.edit(server);
            fail();
        } catch(CytomineException e) {
            assertFalse(e.code+" should be 4xx", e.code == 200);
        }

    }

    @Test
    public void testDelete() throws Exception {
        Project project = new Project();
        project.build("TESTDELETE-"+date,ID_ONTOLOGY,false,true,new ArrayList(),new ArrayList());
        project.add(server);
        assertNotNull(project.getId());
        Long id = project.getId();
        project.delete(server);
        assertNull(project.getId());

        try {
            Project.get(server,id);
            fail();
        } catch(CytomineException e) {
            assertTrue(e.code + " should be 404", e.code == 404);
        }

        try {
            project.delete(server);
            fail();
        } catch(CytomineException e) {
            assertTrue(e.code+" should be 404", e.code == 404);
        }

    }

    @Test
    public void testSave() throws Exception {
        Project project = new Project();
        List admins = new ArrayList();
        admins.add(42l);//lrollus

        project.build("TESTSAVE-"+date,ID_ONTOLOGY,false,true,admins,new ArrayList());

        assertNull(project.getId());
        project.save(server);
        assertNotNull(project.getId());
        project.setName("TESTSAVE-EDITED-"+date);
        project.save(server);
        assertNotNull(project.getId());
        assertEquals("TESTSAVE-EDITED-"+date,project.getName());


    }

    @Test
    public void testList() throws Exception {
        List list = Project.list(server);
        assertEquals(list.size() + " items", true, list.size() >= 1);
        assertTrue(contains(list, ID_PROJECT));
        assertFalse(contains(list, -1l));

        list = Project.list(server,1,1);
        assertEquals(list.size() + " items", 1, list.size());
    }

    @Test
    public void testListByOntology() throws Exception {
        List list = Project.listByOntology(server, ID_ONTOLOGY);
        assertEquals(list.size() + " items", true, list.size() >= 1);
        for(int i=0;i<list.size();i++) {
            assertTrue(((Project)list.get(i)).getOntology().equals(ID_ONTOLOGY));
        }
    }
     

    public boolean contains(List list, Long id) throws Exception{
        boolean exist = false;
        for(int i=0;i<list.size();i++) {
            System.out.println((((Project)list.get(i)).getId()) + "=>" + (((AbstractDomain)list.get(i)).getId().equals(id)));
            exist = exist || (((AbstractDomain)list.get(i)).getId().equals(id));
        }
        return exist;
    }

//
//    @Test
//    public void testEquals() throws Exception {
//        Project project1 = Project.build(json57);
//        Project project2 = Project.get(json57.id)
//        Project project3 = new Project("NotEquals");
//        Project project4 = Project.build(json57);
//
//        EqualsTester(project1, project2, project3, project4)
//    }
}
