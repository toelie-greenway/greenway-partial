package greenway_myanmar.org.util;

import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.List;

import greenway_myanmar.org.R;
import greenway_myanmar.org.vo.Sticker;

public class StickerUtil {

    public static final List<Sticker> STICKERS = createStickerList();
    private static final ArrayMap<String, Sticker> STICKERS_MAP = createStickerMap();

    private static List<Sticker> createStickerList() {
        List<Sticker> stickers = new ArrayList<>();
        stickers.add(new Sticker("GW_SP1_01", R.drawable.ic_sticker_pack1_mingalarpar));
        stickers.add(new Sticker("GW_SP1_02", R.drawable.ic_sticker_pack1_success));
        stickers.add(new Sticker("GW_SP1_03", R.drawable.ic_sticker_pack1_thanks));
        stickers.add(new Sticker("GW_SP1_04", R.drawable.ic_sticker_pack1_fine));
        stickers.add(new Sticker("GW_SP1_05", R.drawable.ic_sticker_pack1_difficult));
        stickers.add(new Sticker("GW_SP1_06", R.drawable.ic_sticker_pack1_other_method));
        stickers.add(new Sticker("GW_SP1_07", R.drawable.ic_sticker_pack1_perdom_please));
        stickers.add(new Sticker("GW_SP1_08", R.drawable.ic_sticker_pack1_not_ok));
        stickers.add(new Sticker("GW_SP1_09", R.drawable.ic_sticker_pack1_bye));
        stickers.add(new Sticker("GW_SP1_10", R.drawable.ic_sticker_pack1_happy_to_answer));
        stickers.add(new Sticker("GW_SP1_11", R.drawable.ic_sticker_pack1_photo_please));
        stickers.add(new Sticker("GW_SP1_12", R.drawable.ic_sticker_pack1_not_clear));
        stickers.add(new Sticker("GW_SP1_13", R.drawable.ic_sticker_pack1_success_agri));

//        stickers.add(new Sticker("GW_SP2_1", R.drawable.ic_sticker_pack2_asin_pyay_lr));
//        stickers.add(new Sticker("GW_SP2_2", R.drawable.ic_sticker_pack2_asin_pyay_twr_p));
//        stickers.add(new Sticker("GW_SP2_3", R.drawable.ic_sticker_pack2_aww_d_lo_lr));
//        stickers.add(new Sticker("GW_SP2_4", R.drawable.ic_sticker_pack2_d_mhar_wel_loh_ya));
//        stickers.add(new Sticker("GW_SP2_5", R.drawable.ic_sticker_pack2_easy));
//        stickers.add(new Sticker("GW_SP2_6", R.drawable.ic_sticker_pack2_hel_mite_tal));
//        stickers.add(new Sticker("GW_SP2_7", R.drawable.ic_sticker_pack2_how_much));
//        stickers.add(new Sticker("GW_SP2_8", R.drawable.ic_sticker_pack2_take_care));
//        stickers.add(new Sticker("GW_SP2_9", R.drawable.ic_sticker_pack2_where_can_i_buy));
//        stickers.add(new Sticker("GW_SP2_10", R.drawable.ic_sticker_pack2_wow));
        return stickers;
    }

    private static ArrayMap<String, Sticker> createStickerMap() {
        ArrayMap<String, Sticker> stickerMap = new ArrayMap<>();
        for (Sticker sticker : STICKERS) {
            stickerMap.put(sticker.getId(), sticker);
        }
        return stickerMap;
    }

    public static Sticker getStickerById(String stickerId) {
        return STICKERS_MAP.get(stickerId);
    }
}
