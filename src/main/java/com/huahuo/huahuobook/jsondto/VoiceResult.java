package com.huahuo.huahuobook.jsondto;

import lombok.Data;

@Data
public class VoiceResult {
    String date;
    String[] item;
    String[] itemType;
    Double money;
    String pos;
}
