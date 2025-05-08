package me.wsehsatnoen.tacocloud.repositories;

import me.wsehsatnoen.tacocloud.tacos.TacoOrder;
import java.util.Optional;


public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
