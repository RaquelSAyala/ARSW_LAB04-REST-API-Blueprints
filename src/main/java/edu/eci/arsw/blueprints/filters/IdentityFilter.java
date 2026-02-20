package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Filtro por defecto: retorna el blueprint sin modificaciones.
 * Solo activo cuando NO se usan los perfiles 'redundancy' ni 'undersampling'.
 */
@Component
@Profile("!(redundancy | undersampling)")
public class IdentityFilter implements BlueprintsFilter {
    @Override
    public Blueprint apply(Blueprint bp) {
        return bp;
    }
}
