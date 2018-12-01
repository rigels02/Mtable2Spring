package org.rb.mtable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.rb.mtable.model.MTable;
import org.rb.mtable.model.TableData;
import org.rb.mtable.repositories.IMtableCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Mtable2SpringApplication {
    private static final Logger log= LoggerFactory.getLogger(Mtable2SpringApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Mtable2SpringApplication.class, args);
	}
        
        /**/
	@Bean
	public CommandLineRunner demo(IMtableCrudRepository crudRepo){

		return args -> {
			log.info("Save some MTable tables.....");
                    List<MTable> tables = makeTables(-1);
                    /*for (MTable table : tables) {
                    crudRepo.save(table);
                    }*/
                    crudRepo.save(tables);
		};
	}
	/**/
        public static List<MTable> makeTables(int v){
        List<MTable> mtables = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            MTable table = new MTable();
            //table.setId(i);
            table.setName("Table_"+i);
            table.setModTime(new Date());
            if(i != v){
            table.getData().add(new TableData(new Date(117,1,10), "Table_" + i + "_Cat1", 10.0, "Note_1"));
            table.getData().add(new TableData(new Date(117,2,20), "Table_" + i + "_Cat2", 20.0, "Note_2"));
            table.getData().add(new TableData(new Date(117,3,21), "Table_" + i + "_Cat3", 30.0, "Note_3"));
            }
            mtables.add(table);
        }
        return mtables;
}
}
