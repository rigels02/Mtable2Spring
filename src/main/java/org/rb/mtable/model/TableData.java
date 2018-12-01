
package org.rb.mtable.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *Created on 27-Feb-17.
 * @author raitis
 */
//@Embeddable
@Entity
@NamedQueries(
        {
    @NamedQuery(name = "TableData.findAll", query = "select p from TableData p"),
    @NamedQuery(name = "TableData.findAllById", query = "select p from TableData p where p.Id = :id")
        
}
)
public class TableData implements Serializable, Cloneable{
    
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)        
    private long Id;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date cdate;
    @NotBlank
    private String cat;
    @NotNull
    private Double amount;
    private String note;

    public TableData() {
    }

    public TableData(Date cdate, String cat, Double amount, String note) {
        this.cdate = cdate;
        this.cat = cat;
        this.amount = amount;
        this.note = note;
    }

private TableData(TableData other) {
        this.Id = other.Id;
        this.cdate = other.cdate;
        this.cat = other.cat;
        this.amount = other.amount;
        this.note = other.note;  
    }    

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    /**
     * Make shallow copy 
     * Should be overriden in subclasses
     * @return 
     */
    public TableData mkCopy(){
        return new TableData(this);
     }
    /**
     * Make deep copy
     *  Should be overriden in subclasses
     * @return
     * @throws Exception 
     */
    public TableData deepCopy() throws Exception {
        //Check if T is instance of Serializeble other throw CloneNotSupportedException
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //Serialize it
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

        //Deserialize it and return the new instance
        return (TableData) ois.readObject();
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
    public TableData mkCloaning() throws CloneNotSupportedException{
        TableData data = (TableData) this.clone();
        data.setCdate((Date) this.getCdate().clone());
       return data;
    }
    
    @Override
    public String toString() {
        return "TableData{cdate=" + cdate + ", cat=" + cat + ", amount=" + amount + ", note=" + note + '}';
    }
    

    
    
}
