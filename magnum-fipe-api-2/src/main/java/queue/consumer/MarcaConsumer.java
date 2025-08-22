package queue.consumer;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yan.application.ProcessarMarcasService;
import org.yan.domain.model.marca.Marca;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MarcaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaConsumer.class);

    @Inject
    ProcessarMarcasService processarMarcasService;

    @Incoming("marcas-in")
    public void consumirMarcas(JsonArray marcasJson) {
        LOG.info("Mensagem recebida do canal 'marcas-in'. Delegando para o serviço de processamento.");
        List<Marca> marcas = marcasJson.stream()
                .map(obj -> ((JsonObject) obj).mapTo(Marca.class))
                .collect(Collectors.toList());
        processarMarcasService.executar(marcas);
    }
}
