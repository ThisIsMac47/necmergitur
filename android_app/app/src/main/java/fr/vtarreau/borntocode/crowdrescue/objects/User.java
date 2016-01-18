package fr.vtarreau.borntocode.crowdrescue.objects;

import java.util.UUID;

/**
 * Created by Vincent on 16/01/2016.
 */

public class User {
        public UUID id;
        public String 	name;
        public String	phone;
        public String	mail;
        public Short    password;
        public String   last_geo;

        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public Short getPassword() {
                return password;
        }

        public void setPassword(Short password) {
                this.password = password;
        }

        public String getLast_geo() {
                return last_geo;
        }

        public void setLast_geo(String last_geo) {
                this.last_geo = last_geo;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getMail() {
                return mail;
        }

        public void setMail(String mail) {
                this.mail = mail;
        }
}
