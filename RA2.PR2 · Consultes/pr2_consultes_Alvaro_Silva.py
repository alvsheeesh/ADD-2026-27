"""RA2.PR2 · Consultes — Álvaro Silva

Consultes de la pantalla principal de la biblioteca. Cap funció imprimeix res:
totes retornen objectes i és el main qui els mostra.
"""
from contextlib import closing
from dataclasses import dataclass
from datetime import date

from db import connectar


@dataclass
class Llibre:
    """Un llibre amb el nom del seu autor (la classe de c07)."""
    id: int
    titol: str
    any_pub: int
    disponibles: int
    autor: str


@dataclass
class Retard:
    """Un préstec que encara no s'ha tornat i quants dies fa que dura."""
    soci: str
    titol: str
    data_prestec: date
    dies: int


@dataclass
class LlibrePrestat:
    """Un llibre i quantes vegades s'ha prestat."""
    titol: str
    vegades: int


# ---------------------------------------------------------------- PART 1

def llibres_de_autor(cn, nom):
    """Retorna els llibres dels autors que tenen aquest text al nom (cerca amb LIKE)."""
    with closing(cn.cursor(dictionary=True)) as cur:
        # JOIN autor-llibre: INNER, perquè tot llibre té autor (autor_id és NOT NULL amb FK).
        cur.execute("""
            SELECT l.id, l.titol, l.any_pub, l.disponibles, a.nom AS autor
              FROM llibre l
              JOIN autor a ON a.id = l.autor_id
             WHERE a.nom LIKE %s
             ORDER BY l.titol""",
            (f"%{nom}%",))
        return [Llibre(**fila) for fila in cur.fetchall()]

# ---------------------------------------------------------------- PART 2
def socis_amb_retard(cn, dies):
    """Retorna els préstecs no tornats que fa més de «dies» dies que duren, del més antic al més nou."""
    with closing(cn.cursor(dictionary=True)) as cur:
        # JOIN prestec-soci: INNER, perquè tot préstec té un soci (soci_id és NOT NULL amb FK).
        # JOIN prestec-llibre: INNER, perquè tot préstec té un llibre (llibre_id és NOT NULL amb FK).
        cur.execute("""
            SELECT s.nom AS soci, l.titol, p.data_prestec,
              DATEDIFF(CURDATE(), p.data_prestec) AS dies
              FROM prestec p
              JOIN soci s   ON s.id = p.soci_id
              JOIN llibre l ON l.id = p.llibre_id
             WHERE p.data_retorn is NULL
               AND DATEDIFF(CURDATE(), p.data_prestec) > %s
             ORDER BY dies DESC""",
            (dies,))
        return [Retard(**fila) for fila in cur.fetchall()]


# ---------------------------------------------------------------- PART 3
def top_llibres(cn, n):
    """Retorna els n llibres més prestats, del que més al que menys (si empaten, per títol)."""
    with closing(cn.cursor(dictionary=True)) as cur:
        # JOIN llibre-prestec: LEFT, perquè així també surten els llibres que no s'han prestat mai (amb 0).
        cur.execute("""
            SELECT l.titol, COUNT(p.id) AS vegades
              FROM llibre l
              LEFT JOIN prestec p ON p.llibre_id = l.id
             GROUP BY l.id, l.titol
             ORDER BY vegades DESC, l.titol
             LIMIT %s""",
            (n,))
        return [LlibrePrestat(**fila) for fila in cur.fetchall()]


# ---------------------------------------------------------------- PART 4
def llibres_esgotats(cn):
    """Retorna els llibres que no tenen cap exemplar disponible, amb el nom de l'autor."""
    with closing(cn.cursor(dictionary=True)) as cur:
        # JOIN llibre-autor: INNER, perquè tot llibre té autor (autor_id és NOT NULL amb FK).
        cur.execute("""
            SELECT l.id, l.titol, l.any_pub, l.disponibles, a.nom AS autor
              FROM llibre l
              JOIN autor a ON a.id = l.autor_id
             WHERE l.disponibles = 0
             ORDER BY l.titol""")
        return [Llibre(**fila) for fila in cur.fetchall()]


if __name__ == "__main__":
    with closing(connectar()) as cn:          # una sola connexió per a totes les consultes
        print("--- 1. Llibres d'un autor que conté 'rodoreda' ---")
        for llibre in llibres_de_autor(cn, "rodoreda"):
            print(f"  {llibre.titol} ({llibre.any_pub}) - {llibre.autor}")

        print("\n--- 2. Préstecs pendents de més de 30 dies ---")
        for r in socis_amb_retard(cn, 30):
            print(f"  {r.soci:<12} {r.titol:<22} des del {r.data_prestec} ({r.dies} dies)")

        print("\n--- 3. Els 3 llibres més prestats ---")
        for t in top_llibres(cn, 3):
            print(f"  {t.titol:<22} {t.vegades} préstecs")

        print("\n--- 4. Llibres esgotats ---")
        for llibre in llibres_esgotats(cn):
            print(f"  {llibre.titol} - {llibre.autor}")
