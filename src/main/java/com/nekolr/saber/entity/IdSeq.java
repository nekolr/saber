package com.nekolr.saber.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@ToString
@Entity
@Table(name = "id_seq")
public class IdSeq implements Serializable {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_sequence")
    @SequenceGenerator(name = "id_seq_sequence", initialValue = 329, allocationSize = 1)
    @Id
    private Long id;
}
