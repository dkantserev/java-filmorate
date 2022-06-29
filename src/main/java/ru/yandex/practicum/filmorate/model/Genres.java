package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Genres {
   private final int id;
   private final String name;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Genres genres = (Genres) o;

      return id == genres.id;
   }

   @Override
   public int hashCode() {
      return id;
   }
}
