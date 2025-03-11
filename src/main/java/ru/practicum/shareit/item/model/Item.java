package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    @NotBlank(message = "Имя не должно быть пустым")
    String name;
    @Column(name = "description")
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @Column(name = "available")
    @NotNull(message = "Должен присутствовать статус")
    Boolean available;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    User owner;
    @Column(name = "request_id")
    Long request;
}
