package com.digte.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Whatsapp {
    @XmlElement public String to;
    @XmlElement public String from;
    @XmlElement public String body;
}