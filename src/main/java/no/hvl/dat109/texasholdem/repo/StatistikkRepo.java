package no.hvl.dat109.texasholdem.repo;

import no.hvl.dat109.texasholdem.Enums.HaandKombinasjon;
import no.hvl.dat109.texasholdem.entity.Statistikk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatistikkRepo extends JpaRepository<Statistikk,Integer> {
    List<Statistikk> finnAntallVunnetHenderStorreEnn(int antallVunnet);
    List<Statistikk> findByHoyesteHaand(HaandKombinasjon haandKombinasjon);

    List<Statistikk> finnSpillereMedPositivNettoGevinst();

    Optional<Statistikk> findByRunde(int runde);

}
