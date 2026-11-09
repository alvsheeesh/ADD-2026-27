"""RA2.PR1 · Manteniment de socis — Álvaro Silva

Menú al terminal per donar d'alta, canviar el nom i donar de baixa socis de
la BD biblioteca. Totes les funcions reben la connexió (cn) que obre el main.
"""
from contextlib import closing

import mysql.connector
from mysql.connector import errorcode

from db import connectar


def alta_soci(cn, nom, email):
    """Dona d'alta un soci i retorna l'id que li ha donat AUTO_INCREMENT.
    Si l'email ja existeix (errno 1062), fa rollback, avisa i retorna None."""
    try:
        with closing(cn.cursor()) as cur:
            cur.execute("INSERT INTO soci (nom, email) VALUES (%s, %s)",
                        (nom, email))
            cn.commit()
            return cur.lastrowid

    except mysql.connector.IntegrityError as e:
        cn.rollback()
        if e.errno == errorcode.ER_DUP_ENTRY:
            print(f"Error: ja existeix un soci amb l'email {email}")
        else:
            print(f"Error inesperat a l'alta (errno {e.errno})")
        return None


def canviar_nom(cn, id, nom):
    """Canvia el nom del soci. Retorna True si ha canviat una fila i False si no:
    el soci no existeix o ja tenia aquest nom (MySQL només compta les files que canvien)."""
    with closing(cn.cursor()) as cur:
        cur.execute("UPDATE soci SET nom = %s WHERE id = %s",
                    (nom, id))
        cn.commit()
        if cur.rowcount == 0:
            print("No s'ha canviat res: el soci no existeix o ja tenia aquest nom")
        return cur.rowcount == 1


def baixa_soci(cn, id):
    """Dona de baixa el soci. Retorna True si l'ha esborrat i False si no existeix o té préstecs:
    la FK fk_prestec_soci no deixa esborrar un soci amb préstecs (IntegrityError, errno 1451)."""
    try:
        with closing(cn.cursor()) as cur:
            cur.execute("DELETE FROM soci WHERE id = %s", (id,))
            cn.commit()
            if cur.rowcount == 0:
                print(f"No existeix cap soci amb id {id}")
            return cur.rowcount == 1

    except mysql.connector.IntegrityError as e:
        cn.rollback()
        if e.errno == errorcode.ER_ROW_IS_REFERENCED_2:
            print(f"No es pot donar de baixa el soci {id}: té préstecs (errno {e.errno})")
        else:
            print(f"Error inesperat a la baixa (errno {e.errno})")
        return False


def llistar_socis(cn):
    """Serveix per llistar els socis."""
    with closing(cn.cursor()) as cur:
        cur.execute("SELECT id, nom, email FROM soci ORDER BY id")
        for id, nom, email in cur.fetchall():
            print(f"  {id:<4}{nom:<16}{email}")
    cn.commit()


def demanar_id():
    """Demana l'id per teclat. Retorna el número, o None si no ho és."""
    try:
        return int(input("Id del soci: "))
    except ValueError:
        print("L'id ha de ser un número")
        return None


if __name__ == "__main__":
    with closing(connectar()) as cn:          # una sola connexió per a tota la sessió
        while True:
            print("\n===== Manteniment de socis =====")
            print("1. Alta")
            print("2. Canviar nom")
            print("3. Baixa")
            print("4. Llistar socis")
            print("0. Sortir")
            opcio = input("Opció: ")

            if opcio == "1":
                nom = input("Nom: ")
                email = input("Email: ")
                nou = alta_soci(cn, nom, email)
                if nou is not None:
                    print(f"Soci creat amb id {nou}")

            elif opcio == "2":
                id = demanar_id()
                if id is not None:
                    nom = input("Nom nou: ")
                    if canviar_nom(cn, id, nom):
                        print("Nom canviat")

            elif opcio == "3":
                id = demanar_id()
                if id is not None and baixa_soci(cn, id):
                    print("Soci donat de baixa")

            elif opcio == "4":
                llistar_socis(cn)
            elif opcio == "0":
                break
            else:
                print("Opció no vàlida")
