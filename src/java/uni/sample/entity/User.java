package uni.sample.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Laszlo Kishalmi
 */
@Entity
@Table(name = "APP_USER")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {

    public enum Group {

        USER, API, ADMIN
    }
    
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 32)
    @Column(length = 32, updatable = false)
    @XmlID
    private String name;
    
    @Column(length = 64)
    @XmlTransient
    private String password;
    
    @NotNull
    @Pattern(regexp = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")
    @Size(min = 3, max = 64)
    @Column(length = 64, nullable = false)
    private String email;
    
    private String fullName;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Set<Group> groups;

    public User() {
        groups = EnumSet.of(Group.USER);
    }
    
    public User(String name, String email, String password, Group... groups) {
        this.name = name;
        this.email = email;
        this.password = hashPassword(password);
        this.groups = EnumSet.noneOf(Group.class);
        this.groups.addAll(Arrays.asList(groups));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Set<Group> getGroups() {
        return groups;
    }
    
    public boolean isAdmin() {
        return groups.contains(Group.ADMIN);
    }

    public String getFullName() {
        return fullName != null ? fullName : name;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", email=" + email + ", groups=" + groups + '}';
    }
    
    public static void main(String[] args) {
        System.out.println("Password Hash: " + hashPassword("admin"));
    }
    
    public static String hashPassword(String passwd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buf = digest.digest(passwd.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < buf.length; i++) {
                int b = 0xFF & buf[i];
                if (b < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            //Shall not happen
            throw new IllegalArgumentException(ex);
        }
    }
}
 
