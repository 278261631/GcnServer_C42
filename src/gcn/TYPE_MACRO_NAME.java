package gcn;

import util.XML2File;

public enum TYPE_MACRO_NAME {
	GRB_COORDS				(1),
	TEST_COORDS			(2),
	IM_ALIVE				(3),
	KILL_SOCKET			(4),
	MAXBC					(11),
	BRAD_COORDS			(21),
	GRB_FINAL			(22),
	HUNTS_SRC			(24),
	ALEXIS_SRC			(25),
	XTE_PCA_ALERT			(26),
	XTE_PCA_SRC			(27),
	XTE_ASM_ALERT			(28),
	XTE_ASM_SRC			(29),
	COMPTEL_SRC			(30),
	IPN_RAW			(31),
	IPN_SEG			(32),
	SAX_WFC_ALERT			(33),
	SAX_WFC_SRC			(34),
	SAX_NFI_ALERT			(35),
	SAX_NFI_SRC			(36),
	XTE_ASM_TRANS			(37),
	spare38			(38),
	IPN_POS			(39),
	HETE_ALERT_SRC			(40),
	HETE_UPDATE_SRC			(41),
	HETE_FINAL_SRC			(42),
	HETE_GNDANA_SRC			(43),
	HETE_TEST			      (44),
	GRB_CNTRPART			  (45),
	SWIFT_TOO_FOM			  (46),
	SWIFT_TOO_SC_SLEW			(47),
	DOW_TOD			        (48),
	spare50			        (50),
	INTEGRAL_POINTDIR			(51),
	INTEGRAL_SPIACS			(52),
	INTEGRAL_WAKEUP			(53),
	INTEGRAL_REFINED			(54),
	INTEGRAL_OFFLINE			(55),
	INTEGRAL_WEAK			  (56),
	AAVSO			          (57),
	MILAGRO_POS			  (58),
	KONUS_LC			      (59),
	SWIFT_BAT_GRB_ALERT			(60),
	SWIFT_BAT_GRB_POS_ACK			(61),
	SWIFT_BAT_GRB_POS_NACK			(62),
	SWIFT_BAT_GRB_LC			(63),
	SWIFT_BAT_SCALEDMAP			(64),
	SWIFT_FOM_OBS			(65),
	SWIFT_SC_SLEW			(66),
	SWIFT_XRT_POSITION			(67),
	SWIFT_XRT_SPECTRUM			(68),
	SWIFT_XRT_IMAGE			(69),
	SWIFT_XRT_LC			(70),
	SWIFT_XRT_CENTROID			(71),
	SWIFT_UVOT_DBURST			(72),
	SWIFT_UVOT_FCHART			(73),
	SWIFT_BAT_GRB_LC_PROC			(76),
	SWIFT_XRT_SPECTRUM_PROC			(77),
	SWIFT_XRT_IMAGE_PROC			(78),
	SWIFT_UVOT_DBURST_PROC			(79),
	SWIFT_UVOT_FCHART_PROC			(80),
	SWIFT_UVOT_POS			(81),
	SWIFT_BAT_GRB_POS_TEST			(82),
	SWIFT_POINTDIR			(83),
	SWIFT_BAT_TRANS			(84),
	SWIFT_XRT_THRESHPIX			(85),
	SWIFT_XRT_THRESHPIX_PROC			(86),
	SWIFT_XRT_SPER			(87),
	SWIFT_XRT_SPER_PROC			(88),
	SWIFT_UVOT_POS_NACK			(89),
	SWIFT_BAT_ALARM_SHORT			(90),
	SWIFT_BAT_ALARM_LONG			(91),
	SWIFT_UVOT_EMERGENCY			(92),
	SWIFT_XRT_EMERGENCY			(93),
	SWIFT_FOM_PPT_ARG_ERR			(94),
	SWIFT_FOM_SAFE_POINT			(95),
	SWIFT_FOM_SLEW_ABORT			(96),
	SWIFT_BAT_QL_POS			(97),
	SWIFT_BAT_SUB_THRESHOLD			(98),
	SWIFT_BAT_SLEW_POS			(99),
	AGILE_GRB_WAKEUP			(100),
	AGILE_GRB_GROUND			(101),
	AGILE_GRB_REFINED			(102),
	SWIFT_ACTUAL_POINTDIR			(103),
	AGILE_POINTDIR			(107),
	AGILE_TRANS			(108),
	AGILE_GRB_POS_TEST			(109),
	FERMI_GBM_ALERT			(110),
	FERMI_GBM_FLT_POS			(111),
	FERMI_GBM_GND_POS			(112),
	FERMI_GBM_LC			(113),
	FERMI_GBM_GND_INTERNAL			(114),
	FERMI_GBM_FIN_POS			(115),
	FERMI_GBM_TRANS			(118),
	FERMI_GBM_POS_TEST			(119),
	FERMI_LAT_POS_INI			(120),
	FERMI_LAT_POS_UPD			(121),
	FERMI_LAT_POS_DIAG			(122),
	FERMI_LAT_TRANS			(123),
	FERMI_LAT_POS_TEST			(124),
	FERMI_LAT_MONITOR			(125),
	FERMI_SC_SLEW			(126),
	FERMI_LAT_GND			(127),
	FERMI_LAT_OFFLINE			(128),
	FERMI_POINTDIR			(129),
	SIMBADNED			(130),
	PIOTS_OT_POS			(131),
	KAIT_SN			(132),
	SWIFT_BAT_MONITOR			(133),
	MAXI_UNKNOWN			(134),
	MAXI_KNOWN			(135),
	MAXI_TEST			(136),
	OGLE			(137),
	CBAT			(138),
	MOA			(139),
	SWIFT_BAT_SUBSUB			(140),
	SWIFT_BAT_KNOWN_SRC			(141),
	VOE_1_1_IM_ALIVE			(142),
	VOE_2_0_IM_ALIVE			(143),
	COINCIDENCE			(145),
	SUZAKU_LC			(148),
	SNEWS			(149),
	LVC_PRELIM			(150),
	LVC_INITIAL			(151),
	LVC_UPDATE			(152),
	LVC_TEST			(153),
	LVC_CNTRPART			(154),
	AMON_ICECUBE_COINC			(157),
	AMON_ICECUBE_HESE			(158),
	CALET_GBM_FLT_LC			(160),
	CALET_GBM_GND_LC			(161),
//	GWHEN_COINC			(169),
	AMON_ICECUBE_EHE			(169);

	
    private int value;
    
    private TYPE_MACRO_NAME(int value) {
        this.value = value;
    }
 
    public int getValue() {
        return value;
    }
    
    public static TYPE_MACRO_NAME valueOf(int value) {  
        switch (value) {
        case  	1	:	return	GRB_COORDS	;
        case  	2	:	return	TEST_COORDS	;
        case  	3	:	return	IM_ALIVE	;
        case  	4	:	return	KILL_SOCKET	;
        case  	11	:	return	MAXBC	;
        case  	21	:	return	BRAD_COORDS	;
        case  	22	:	return	GRB_FINAL	;
        case  	24	:	return	HUNTS_SRC	;
        case  	25	:	return	ALEXIS_SRC	;
        case  	26	:	return	XTE_PCA_ALERT	;
        case  	27	:	return	XTE_PCA_SRC	;
        case  	28	:	return	XTE_ASM_ALERT	;
        case  	29	:	return	XTE_ASM_SRC	;
        case  	30	:	return	COMPTEL_SRC	;
        case  	31	:	return	IPN_RAW	;
        case  	32	:	return	IPN_SEG	;
        case  	33	:	return	SAX_WFC_ALERT	;
        case  	34	:	return	SAX_WFC_SRC	;
        case  	35	:	return	SAX_NFI_ALERT	;
        case  	36	:	return	SAX_NFI_SRC	;
        case  	37	:	return	XTE_ASM_TRANS	;
        case  	38	:	return	spare38	;
        case  	39	:	return	IPN_POS	;
        case  	40	:	return	HETE_ALERT_SRC	;
        case  	41	:	return	HETE_UPDATE_SRC	;
        case  	42	:	return	HETE_FINAL_SRC	;
        case  	43	:	return	HETE_GNDANA_SRC	;
        case  	44	:	return	HETE_TEST	;
        case  	45	:	return	GRB_CNTRPART	;
        case  	46	:	return	SWIFT_TOO_FOM	;
        case  	47	:	return	SWIFT_TOO_SC_SLEW	;
        case  	48	:	return	DOW_TOD	;
        case  	50	:	return	spare50	;
        case  	51	:	return	INTEGRAL_POINTDIR	;
        case  	52	:	return	INTEGRAL_SPIACS	;
        case  	53	:	return	INTEGRAL_WAKEUP	;
        case  	54	:	return	INTEGRAL_REFINED	;
        case  	55	:	return	INTEGRAL_OFFLINE	;
        case  	56	:	return	INTEGRAL_WEAK	;
        case  	57	:	return	AAVSO	;
        case  	58	:	return	MILAGRO_POS	;
        case  	59	:	return	KONUS_LC	;
        case  	60	:	return	SWIFT_BAT_GRB_ALERT	;
        case  	61	:	return	SWIFT_BAT_GRB_POS_ACK	;
        case  	62	:	return	SWIFT_BAT_GRB_POS_NACK	;
        case  	63	:	return	SWIFT_BAT_GRB_LC	;
        case  	64	:	return	SWIFT_BAT_SCALEDMAP	;
        case  	65	:	return	SWIFT_FOM_OBS	;
        case  	66	:	return	SWIFT_SC_SLEW	;
        case  	67	:	return	SWIFT_XRT_POSITION	;
        case  	68	:	return	SWIFT_XRT_SPECTRUM	;
        case  	69	:	return	SWIFT_XRT_IMAGE	;
        case  	70	:	return	SWIFT_XRT_LC	;
        case  	71	:	return	SWIFT_XRT_CENTROID	;
        case  	72	:	return	SWIFT_UVOT_DBURST	;
        case  	73	:	return	SWIFT_UVOT_FCHART	;
        case  	76	:	return	SWIFT_BAT_GRB_LC_PROC	;
        case  	77	:	return	SWIFT_XRT_SPECTRUM_PROC	;
        case  	78	:	return	SWIFT_XRT_IMAGE_PROC	;
        case  	79	:	return	SWIFT_UVOT_DBURST_PROC	;
        case  	80	:	return	SWIFT_UVOT_FCHART_PROC	;
        case  	81	:	return	SWIFT_UVOT_POS	;
        case  	82	:	return	SWIFT_BAT_GRB_POS_TEST	;
        case  	83	:	return	SWIFT_POINTDIR	;
        case  	84	:	return	SWIFT_BAT_TRANS	;
        case  	85	:	return	SWIFT_XRT_THRESHPIX	;
        case  	86	:	return	SWIFT_XRT_THRESHPIX_PROC	;
        case  	87	:	return	SWIFT_XRT_SPER	;
        case  	88	:	return	SWIFT_XRT_SPER_PROC	;
        case  	89	:	return	SWIFT_UVOT_POS_NACK	;
        case  	90	:	return	SWIFT_BAT_ALARM_SHORT	;
        case  	91	:	return	SWIFT_BAT_ALARM_LONG	;
        case  	92	:	return	SWIFT_UVOT_EMERGENCY	;
        case  	93	:	return	SWIFT_XRT_EMERGENCY	;
        case  	94	:	return	SWIFT_FOM_PPT_ARG_ERR	;
        case  	95	:	return	SWIFT_FOM_SAFE_POINT	;
        case  	96	:	return	SWIFT_FOM_SLEW_ABORT	;
        case  	97	:	return	SWIFT_BAT_QL_POS	;
        case  	98	:	return	SWIFT_BAT_SUB_THRESHOLD	;
        case  	99	:	return	SWIFT_BAT_SLEW_POS	;
        case  	100	:	return	AGILE_GRB_WAKEUP	;
        case  	101	:	return	AGILE_GRB_GROUND	;
        case  	102	:	return	AGILE_GRB_REFINED	;
        case  	103	:	return	SWIFT_ACTUAL_POINTDIR	;
        case  	107	:	return	AGILE_POINTDIR	;
        case  	108	:	return	AGILE_TRANS	;
        case  	109	:	return	AGILE_GRB_POS_TEST	;
        case  	110	:	return	FERMI_GBM_ALERT	;
        case  	111	:	return	FERMI_GBM_FLT_POS	;
        case  	112	:	return	FERMI_GBM_GND_POS	;
        case  	113	:	return	FERMI_GBM_LC	;
        case  	114	:	return	FERMI_GBM_GND_INTERNAL	;
        case  	115	:	return	FERMI_GBM_FIN_POS	;
        case  	118	:	return	FERMI_GBM_TRANS	;
        case  	119	:	return	FERMI_GBM_POS_TEST	;
        case  	120	:	return	FERMI_LAT_POS_INI	;
        case  	121	:	return	FERMI_LAT_POS_UPD	;
        case  	122	:	return	FERMI_LAT_POS_DIAG	;
        case  	123	:	return	FERMI_LAT_TRANS	;
        case  	124	:	return	FERMI_LAT_POS_TEST	;
        case  	125	:	return	FERMI_LAT_MONITOR	;
        case  	126	:	return	FERMI_SC_SLEW	;
        case  	127	:	return	FERMI_LAT_GND	;
        case  	128	:	return	FERMI_LAT_OFFLINE	;
        case  	129	:	return	FERMI_POINTDIR	;
        case  	130	:	return	SIMBADNED	;
        case  	131	:	return	PIOTS_OT_POS	;
        case  	132	:	return	KAIT_SN	;
        case  	133	:	return	SWIFT_BAT_MONITOR	;
        case  	134	:	return	MAXI_UNKNOWN	;
        case  	135	:	return	MAXI_KNOWN	;
        case  	136	:	return	MAXI_TEST	;
        case  	137	:	return	OGLE	;
        case  	138	:	return	CBAT	;
        case  	139	:	return	MOA	;
        case  	140	:	return	SWIFT_BAT_SUBSUB	;
        case  	141	:	return	SWIFT_BAT_KNOWN_SRC	;
        case  	142	:	return	VOE_1_1_IM_ALIVE	;
        case  	143	:	return	VOE_2_0_IM_ALIVE	;
        case  	145	:	return	COINCIDENCE	;
        case  	148	:	return	SUZAKU_LC	;
        case  	149	:	return	SNEWS	;
        case  	150	:	return	LVC_PRELIM	;
        case  	151	:	return	LVC_INITIAL	;
        case  	152	:	return	LVC_UPDATE	;
        case  	153	:	return	LVC_TEST	;
        case  	154	:	return	LVC_CNTRPART	;
        case  	157	:	return	AMON_ICECUBE_COINC	;
        case  	158	:	return	AMON_ICECUBE_HESE	;
        case  	160	:	return	CALET_GBM_FLT_LC	;
        case  	161	:	return	CALET_GBM_GND_LC	;
//        case  	169	:	return	GWHEN_COINC	;
        case  	169	:	return	AMON_ICECUBE_EHE	;


        default: 
            return null;
        }
    }
}


