
CREATE TABLE if not EXISTS teams (
    id INTEGER PRIMARY KEY,
    name varchar(255),
    fullName varchar(255),
    managerId
);

CREATE TABLE if not EXISTS managers (
    id INTEGER PRIMARY KEY,
    name varchar(255),
    elo int DEFAULT 1200,
    teamId int,
    strikes int DEFAULT 0
);

INSERT into managers (name, elo)
VALUES
("@Riverside1984",1200),
("@CaptPetrenko",1200),
("@Nautilus90",1200),
("@Zheyda",1200),
("@SviatL",1200),
("@haykosar",1200),
("@MarcusHoper",1200),
("@maga_rs",1200),
("@Petrenko_AV1",1200),
("@welldoneson",1200),
("@betup96",1200),
("@Asttema",1200),
("@Tennessee_Titans",1200),
("@kapilavastu",1200),
("@EvgPurine",1200),
("@Kobza89",1200),
("@wicked_kiD",1200),
("@Alf0981",1200),
("@vitebsk_lynxes_coach",1200),
("@Arturetti",1200),
("@KesGod",1200),
("@shchastye",1200),
("@DimmiDi",1200),
("@PauloDi",1200),
("@lespaul88",1200),
("@drewmeng",1200),
("@Archi059",1200),
("@JesterSV",1200),
("@Crusaders12Lutsk",1200),
("@tpgs77",1200),
("@BAJlEPA_23",1200),
("@gorskingb",1200);



INSERT into teams (name, fullName)
VALUES
("CHI","Bears"),
("CIN","Bengals"),
("BUF","Bills"),
("DEN","Broncos"),
("CLE","Browns"),
("TB","Buccaneers"),
("ARI","Cardinals"),
("LAC","Chargers"),
("KC","Chiefs"),
("IND","Colts"),
("DAL","Cowboys"),
("MIA","Dolphins"),
("PHI","Eagles"),
("ATL","Falcons"),
("SF","49ers"),
("NYG","Giants"),
("JAX","Jaguars"),
("NYJ","Jets"),
("DET","Lions"),
("GB","Packers"),
("CAR","Panthers"),
("NE","Patriots"),
("LV","Raiders"),
("LAR","Rams"),
("BAL","Ravens"),
("WAS","Football Team"),
("NO","Saints"),
("SEA","Seahawks"),
("PIT","Steelers"),
("TEN","Titans"),
("MIN","Vikings"),
("HOU","Texans");

AFC

PIT | @Riverside1984
CIN | @CaptPetrenko
CLE | @Nautilus90
BAL | @Zheyda

NE  | @SviatL
MIA | @haykosar
BUF | @MarcusHoper
NYJ | @maga_rs

OAK | @Petrenko_AV1
DEN | @welldoneson
LAC | @betup96
KC  | @Asttema

TEN | @Tennessee_Titans
IND | @kapilavastu
HOU | @EvgPurine
JAX | @Kobza89

NFC

PHI | @wicked_kiD
NYG | @Alf0981
WAS | @vitebsk_lynxes_coach
DAL | @Arturetti

ATL | @KesGod
TB  | @shchastye
CAR | @DimmiDi
NO  | @PauloDi

MIN | @lespaul88
DET | @drewmeng
CHI | @Archi059
GB  | @JesterSV

SEA | @Crusaders12Lutsk
SF  | @tpgs77
LAR | @BAJlEPA_23
ARI | @gorskingb




