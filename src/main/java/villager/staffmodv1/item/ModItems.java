package YOUR_PACKAGE.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import YOUR_PACKAGE.item.custom.VillagerStaffItem;

public class ModItems {
    public static final Item VILLAGER_STAFF = registerItem("villager_staff",
            new VillagerStaffItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("yourmodid", name), item);
    }

    public static void registerModItems() {
        System.out.println("Registering Mod Items for yourmodid");
    }
}
