package org.rb.mtable.controller;


import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
import org.rb.mtable.model.MTable;
import org.rb.mtable.repositories.IMtableCrudRepository;
import org.springframework.validation.BindingResult;

/**
 * Created by raitis on 27-Feb-17.
 */
@RestController
@RequestMapping("/ctables")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class MTableController {

    @Autowired
    private IMtableCrudRepository crudRepo;

    @RequestMapping(path ="/dummy" ,method = RequestMethod.GET)
    public ResponseEntity<MTable> getDummy(){
        MTable table = new MTable();
        table.setName("DummyTable");
        table.setModTime(new Date());
        return new ResponseEntity<>(table , HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MTable>> getAllMTables(){
       return new ResponseEntity<>((List<MTable>) crudRepo.findAll(), HttpStatus.OK);
    }
    
    @RequestMapping(path = "{id}",method = RequestMethod.GET)
    public ResponseEntity<MTable> getMTableById(@PathVariable(value = "id") long id){
        return new ResponseEntity<>((MTable) crudRepo.findOne(id), HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MTable> createMTable(@RequestBody @Valid MTable table, BindingResult bindResult){
        if(bindResult.hasFieldErrors()){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>((MTable) crudRepo.save(table), HttpStatus.CREATED);
    }
    
    @RequestMapping(path = "{id}",method = RequestMethod.PUT)
    public ResponseEntity<MTable> modifyMTableById
        (   @PathVariable(value = "id") long id,
            @RequestBody @Valid MTable table,
            BindingResult bindResult
        ) 
    {
        if(bindResult.hasFieldErrors()){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if( crudRepo.findOne(id) ==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            crudRepo.delete(id);
            return new ResponseEntity<>((MTable) crudRepo.save(table), HttpStatus.OK);
        }
    }
        
    @RequestMapping(path = "{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMTableById(@PathVariable(value = "id") long id) {
        if( crudRepo.findOne(id) ==null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }else {
            crudRepo.delete(id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
    }

}
