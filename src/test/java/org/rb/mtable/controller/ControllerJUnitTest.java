
package org.rb.mtable.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import org.rb.mtable.Mtable2SpringApplication;
import org.rb.mtable.model.MTable;
import org.rb.mtable.model.TableData;
import org.rb.mtable.repositories.IMtableCrudRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author raitis
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerJUnitTest {
    
    @Autowired
	private TestRestTemplate restTemplate;
    
    @Autowired
    private IMtableCrudRepository crudRepo;
    
    public ControllerJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        crudRepo.deleteAll();
        List<MTable> tables = Mtable2SpringApplication.makeTables(-1);
        for (MTable mtable : tables) {
            crudRepo.save(mtable);
        }
    }
    
    @After
    public void tearDown() {
    }

     @Test
    public void testDummy() {
        ResponseEntity<MTable> entity = this.restTemplate.getForEntity("/ctables/dummy", MTable.class);
                assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody().getName()).isEqualTo("DummyTable");
    }
     @Test
    public void testGetAllTables() {
        ResponseEntity<MTable[]> entity = this.restTemplate.getForEntity("/ctables", MTable[].class);
        List<MTable> expected = Mtable2SpringApplication.makeTables(-1);
        int sz = entity.getBody().length;
        MTable[] tables = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(sz==3);
        int i=0;
         for (MTable mTable : expected) {
             //assertThat(mTable.toString()).isEqualTo(tables[i].toString());
             ValidateTables(expected.get(i),tables[i]);
             i++;
         }
        
    }

     @Test
    public void testGetTableById() {
        ResponseEntity<MTable[]> All = restTemplate.getForEntity("/ctables", MTable[].class);
        ResponseEntity<MTable> entity = this.restTemplate.getForEntity("/ctables/"+All.getBody()[0].getId(), MTable.class);
       
        MTable expected = All.getBody()[0];
        MTable table = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        
             //assertThat(mTable.toString()).isEqualTo(tables[i].toString());
             ValidateTables(expected,table);
                
    }
    @Test
    public void testCreateTable() {
       
        MTable expected = new MTable();
       expected.setName("PostedTable");
       
        Date crDate = new Date();
       expected.setModTime(crDate);
        expected.getData().add(new TableData(crDate,"PostedCat",22.0,"NoteP"));
        
        ResponseEntity<MTable> response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);       
        
        MTable table = response.getBody();
        ValidateTables(expected,table);
                
    }
    @Test
    public void testCreateTableDataAndValidation() {
       
        MTable expected = new MTable();
       expected.setName("PostedTable");
       
        Date crDate = new Date();
       expected.setModTime(crDate);
        expected.getData().add(new TableData(null,"PostedCat",22.0,"NoteP"));
        //date==NULL
        ResponseEntity<MTable> response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);       
        expected.getData().clear();
         expected.getData().add(new TableData(new Date(),"   ",22.0,"NoteP"));
        //cat is Blank
        response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);      
         expected.getData().clear();
         expected.getData().add(new TableData(new Date(),"Cat",null,"NoteP"));
         //amount null
         response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);      
         expected.getData().clear();
         expected.getData().add(new TableData(new Date(),"Cat",12.0,null));
         //Note can be NULL
         response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);      
        
    }
    @Test
    public void testCreateTableInvalidFields() {
       
        MTable expected = new MTable();
       //Blank not allowed
        expected.setName("");
       
        Date crDate = new Date();
       expected.setModTime(crDate);
        expected.getData().add(new TableData(crDate,"PostedCat",22.0,"NoteP"));
        
        ResponseEntity<MTable> response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);       
        
        //MTable table = response.getBody();
        //ValidateTables(expected,table);
                
    }
    @Test
    public void testModifyById(){
        ResponseEntity<MTable[]> All = restTemplate.getForEntity("/ctables", MTable[].class);
        ResponseEntity<MTable> response = this.restTemplate.getForEntity("/ctables/"+All.getBody()[0].getId(), MTable.class);
       
        MTable table = response.getBody();
        long Id = table.getId();
        table.setName("ModifiedName");
        HttpEntity<MTable> entity = new HttpEntity<MTable>(table);
        response = restTemplate.exchange("/ctables/"+Id,HttpMethod.PUT, entity, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);  
        assertEquals(response.getBody().getName(),table.getName() );
        //Id out of range
         response = restTemplate.exchange("/ctables/99",HttpMethod.PUT, entity, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        //Wrong fields, null
        table= new MTable();
        entity = new HttpEntity<MTable>(table);
        response = restTemplate.exchange("/ctables/"+Id,HttpMethod.PUT, entity, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
    }
    
    @Test
    public void testDeleteById(){
        int sz = Mtable2SpringApplication.makeTables(-1).size();
        MTable table = new MTable();
        HttpEntity<MTable> entity = new HttpEntity<MTable>(table);
        
        ResponseEntity<MTable> response = restTemplate.exchange("/ctables/99",HttpMethod.DELETE, entity, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);  
        //--------- create for delete test
        MTable expected = new MTable();
       expected.setName("PostedTable");
       
        Date crDate = new Date();
       expected.setModTime(crDate);
        expected.getData().add(new TableData(new Date(),"PostedCat",22.0,"NoteP"));
        response = this.restTemplate.postForEntity("/ctables", expected, MTable.class);
         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
         //--------- create for delete
        long Id = response.getBody().getId();
        //delete
         response = restTemplate.exchange("/ctables/"+Id,HttpMethod.DELETE, entity, MTable.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //size
        ResponseEntity<MTable[]> all = this.restTemplate.getForEntity("/ctables", MTable[].class);
        assertThat(all.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(all.getBody().length== sz);
    }
    
    private void ValidateTables(MTable mTable, MTable table) {
        Calendar calEx = Calendar.getInstance();
        Calendar calAct = Calendar.getInstance();
        
        assertEquals(mTable.getName(), table.getName());
        int sz = mTable.getData().size();
        for(int i = 0; i< sz;i++){
        assertEquals(mTable.getData().get(i).getCat(), table.getData().get(i).getCat());
        assertEquals(mTable.getData().get(i).getAmount(), table.getData().get(i).getAmount());
        assertEquals(mTable.getData().get(i).getNote(), table.getData().get(i).getNote());
        
        calEx.setTime(mTable.getData().get(i).getCdate());
        calAct.setTime(table.getData().get(i).getCdate());
        assertEquals(calEx.get(Calendar.DAY_OF_MONTH), calAct.get(Calendar.DAY_OF_MONTH));
        assertEquals(calEx.get(Calendar.MONTH), calAct.get(Calendar.MONTH));
        assertEquals(calEx.get(Calendar.YEAR), calAct.get(Calendar.YEAR));
        }
    }
}
