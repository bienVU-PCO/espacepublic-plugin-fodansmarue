/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.fodansmarue.business.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalement;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;

/**
 * The Class Signalement.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Signalement implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 463361533884052226L;

    /** Workflow resource type. */
    public static final String WORKFLOW_RESOURCE_TYPE = "SIGNALEMENT_SIGNALEMENT";

    /** The adresses. */
    private List<Adresse> _adresses = new ArrayList<>( );

    /** The equipements. */
    private Equipement _equipement;

    /** The arrondissement. */
    private Arrondissement _arrondissement;

    /** The date creation. */
    private String _dateCreation;

    /** The _heure creation. */
    private Date _heureCreation;

    /** The id. */
    private Long _lId;

    /** The annee. */
    private int _nAnnee;

    /** The numero. */
    private int _nNumero;

    /** The suivi. */
    private int _nSuivi;

    /** The congratulation. */
    private int _nFelicitations;

    /** The photos. */
    private List<PhotoDMR> _photos = new ArrayList<>( );

    /** The priorite. */
    private Priorite _priorite;

    /** The signaleurs list. */
    private List<Signaleur> _signaleurs = new ArrayList<>( );

    /** The commentaire. */
    private String _strCommentaire;

    /** The commentaire programmation. */
    private String _strCommentaireProgrammation = "";

    /** The date prevue traitement. */
    private String _strDatePrevueTraitement;

    /** The mois. */
    private String _strMois;

    /** The prefix. */
    private String _strPrefix;

    /** The type signalement. */
    private TypeSignalement _typeSignalement;

    /** The unit. */
    private Unit _unit = new Unit( );

    /** direction. */
    private Unit _direction;

    /** token. */
    private String _strToken;

    /** the date of passage. */
    private String _strDateServiceFaitTraitement;

    /** the time of passage. */
    private String _strHeureServiceFaitTraitement;

    /** The observations rejet. */
    private List<ObservationRejet> _observationsRejet;

    /** The attribute to check if the signalement is a duplicate. */
    private boolean _bIsDoublon;

    /** The str commentaire agent terrain. */
    private String _strCommentaireAgentTerrain;

    /** nombreFeedback. */
    private int _nNombreFeedback;

    /**
     * Sets the direction.
     *
     * @param direction
     *            the new direction
     */
    public void setDirection( Unit direction )
    {
        _unit = direction;
    }

    /**
     * Sets the direction sector.
     *
     * @param direction
     *            the new direction sector
     */
    public void setDirectionSector( Unit direction )
    {
        _direction = direction;
    }

    /**
     * Generer lien google map.
     *
     * @param result
     *            the result
     * @param adresse
     *            the adresse
     */
    private void genererLienGoogleMap( StringBuilder result, Adresse adresse )
    {
        result.append( "<a class=\"map\" href=\"https://maps.google.fr/?t=h&z=18&q=" );
        result.append( adresse.getLat( ) );
        result.append( "," );
        result.append( adresse.getLng( ) );
        result.append( "+(" );
        result.append( adresse.getAdresse( ) );
        result.append( ")" );
        result.append( "\" target=\"map\">" );
        result.append( adresse.getAdresse( ) );
        result.append( "</a><br /> " );
    }

    /**
     * Gets the adresses.
     *
     * @return the adresses
     */
    public List<Adresse> getAdresses( )
    {
        return _adresses;
    }

    /**
     * Gets the annee.
     *
     * @return the annee
     */
    public int getAnnee( )
    {
        return _nAnnee;
    }

    /**
     * Gets the arrondissement.
     *
     * @return the arrondissement
     */
    public Arrondissement getArrondissement( )
    {
        return _arrondissement;
    }

    /**
     * Gets the commentaire.
     *
     * @return the commentaire
     */
    public String getCommentaire( )
    {
        return _strCommentaire;
    }

    /**
     * Gets the commentaire programmation.
     *
     * @return the commentaire programmation
     */
    public String getCommentaireProgrammation( )
    {
        return _strCommentaireProgrammation;
    }

    /**
     * Gets the date creation.
     *
     * @return the date creation
     */
    public String getDateCreation( )
    {
        return _dateCreation;
    }

    /**
     * Gets the date prevue traitement.
     *
     * @return the date prevue traitement
     */
    public String getDatePrevueTraitement( )
    {
        return _strDatePrevueTraitement;
    }

    /**
     * Gets the direction sector.
     *
     * @return the direction sector
     */
    public Unit getDirectionSector( )
    {
        return _direction;
    }

    /**
     * Gets the heure creation.
     *
     * @return the heureCreation
     */
    public Date getHeureCreation( )
    {
        return _heureCreation;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId( )
    {
        return _lId;
    }

    /**
     * Gets the list adresses.
     *
     * @return the list adresses
     */
    public String getListAdresses( )
    {
        StringBuilder result = new StringBuilder( "" );
        for ( Adresse adresse : _adresses )
        {

            genererLienGoogleMap( result, adresse );
        }
        return result.toString( );
    }

    /**
     * Gets the mois.
     *
     * @return the mois
     */
    public String getMois( )
    {
        return _strMois;
    }

    /**
     * Gets the numero.
     *
     * @return the numero
     */
    public int getNumero( )
    {
        return _nNumero;
    }

    /**
     * Gets the numero signalement.
     *
     * @return the numeroSignalement
     */
    public String getNumeroSignalement( )
    {
        return getPrefix( ) + getAnnee( ) + getMois( ) + getNumero( );
    }

    /**
     * Gets the photos.
     *
     * @return the photos
     */
    public List<PhotoDMR> getPhotos( )
    {
        return _photos;
    }

    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
    public String getPrefix( )
    {
        return _strPrefix;
    }

    /**
     * Gets the priorite.
     *
     * @return the priorite
     */
    public Priorite getPriorite( )
    {
        return _priorite;
    }

    /**
     * Gets the priorite name.
     *
     * @return the priorite name
     */
    public String getPrioriteName( )
    {
        return _priorite.getLibelle( );
    }

    /**
     * Gets the signaleurs.
     *
     * @return the signaleurs
     */
    public List<Signaleur> getSignaleurs( )
    {
        return _signaleurs;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType( )
    {
        String formatTypeSignalement = null;
        if ( _typeSignalement != null )
        {
            formatTypeSignalement = _typeSignalement.getFormatTypeSignalement( );
        }
        return formatTypeSignalement;
    }

    /**
     * Gets the type signalement.
     *
     * @return the type signalement
     */
    public TypeSignalement getTypeSignalement( )
    {
        return _typeSignalement;
    }

    /**
     * Gets the unit.
     *
     * @return the unit
     */
    public Unit getUnit( )
    {
        return _unit;
    }

    /**
     * Gets the number of followers.
     *
     * @return the number of followers
     */
    public int getSuivi( )
    {
        return _nSuivi;
    }

    /**
     * Sets the adresses.
     *
     * @param pAdresses
     *            the adresses to set
     */
    public void setAdresses( List<Adresse> pAdresses )
    {
        _adresses = pAdresses;
    }

    /**
     * Sets the annee.
     *
     * @param annee
     *            the new annee
     */
    public void setAnnee( int annee )
    {
        _nAnnee = annee;
    }

    /**
     * Sets the arrondissement.
     *
     * @param arrondissement
     *            the new arrondissement
     */
    public void setArrondissement( Arrondissement arrondissement )
    {
        _arrondissement = arrondissement;
    }

    /**
     * Sets the commentaire.
     *
     * @param commentaire
     *            the new commentaire
     */
    public void setCommentaire( String commentaire )
    {
        _strCommentaire = commentaire;
    }

    /**
     * Sets the commentaire programmation.
     *
     * @param commentaireProgrammation
     *            the new commentaire programmation
     */
    public void setCommentaireProgrammation( String commentaireProgrammation )
    {
        _strCommentaireProgrammation = commentaireProgrammation;
    }

    /**
     * Sets the date creation.
     *
     * @param dateCreation
     *            the new date creation
     */
    public void setDateCreation( String dateCreation )
    {
        _dateCreation = dateCreation;
    }

    /**
     * Sets the date prevue traitement.
     *
     * @param datePrevueTraitement
     *            the new date prevue traitement
     */
    public void setDatePrevueTraitement( String datePrevueTraitement )
    {
        _strDatePrevueTraitement = datePrevueTraitement;
    }

    /**
     * Sets the heure creation.
     *
     * @param heureCreation
     *            the heureCreation to set
     */
    public void setHeureCreation( Date heureCreation )
    {
        _heureCreation = heureCreation;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId( Long id )
    {
        _lId = id;
    }

    /**
     * Sets the mois.
     *
     * @param mois
     *            the new mois
     */
    public void setMois( String mois )
    {
        _strMois = mois;
    }

    /**
     * Sets the numero.
     *
     * @param numero
     *            the new numero
     */
    public void setNumero( int numero )
    {
        _nNumero = numero;
    }

    /**
     * Sets the photos.
     *
     * @param pPhotos
     *            the photos to set
     */
    public void setPhotos( List<PhotoDMR> pPhotos )
    {
        _photos = pPhotos;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix
     *            the new prefix
     */
    public void setPrefix( String prefix )
    {
        _strPrefix = prefix;
    }

    /**
     * Sets the priorite.
     *
     * @param priorite
     *            the new priorite
     */
    public void setPriorite( Priorite priorite )
    {
        _priorite = priorite;
    }

    /**
     * Sets the signaleurs.
     *
     * @param pSignaleurs
     *            the signaleurs to set
     */
    public void setSignaleurs( List<Signaleur> pSignaleurs )
    {
        _signaleurs = pSignaleurs;
    }

    /**
     * Sets the type signalement.
     *
     * @param typeSignalement
     *            the new type signalement
     */
    public void setTypeSignalement( TypeSignalement typeSignalement )
    {
        _typeSignalement = typeSignalement;
    }

    /**
     * Sets the unit.
     *
     * @param unit
     *            the new unit
     */
    public void setUnit( Unit unit )
    {
        _unit = unit;
    }

    /**
     * Sets the number of followers.
     *
     * @param suivi
     *            the new number of followers
     */
    public void setSuivi( int suivi )
    {
        _nSuivi = suivi;
    }

    /**
     * Sets is doublon.
     *
     * @param bIsDoublon
     *            true if the signalement is duplicate
     */
    public void setIsDoublon( boolean bIsDoublon )
    {
        _bIsDoublon = bIsDoublon;
    }

    /**
     * Check if the signalement is duplicated.
     *
     * @return true, if is doublon
     */
    public boolean isDoublon( )
    {
        return _bIsDoublon;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken( )
    {
        return _strToken;
    }

    /**
     * Sets the token.
     *
     * @param token
     *            the new token
     */
    public void setToken( String token )
    {
        _strToken = token;
    }

    /**
     * Sets the adresses form.
     *
     * @param index
     *            index
     * @param adresse
     *            the adresse
     */
    public void setAdressesForm( int index, Adresse adresse )
    {
        while ( index >= _adresses.size( ) )
        {
            _adresses.add( new Adresse( ) );
        }

        _adresses.add( index, adresse );
    }

    /**
     * Gets the adresses form.
     *
     * @param index
     *            index
     * @return Adresse
     */
    public Adresse getAdressesForm( int index )
    {
        while ( index >= _adresses.size( ) )
        {
            _adresses.add( new Adresse( ) );
        }

        return _adresses.get( index );
    }

    /**
     * Sets the equipement.
     *
     * @param equipement
     *            the equipement
     */
    public void setEquipement( Equipement equipement )
    {
        _equipement = equipement;
    }

    /**
     * Gets the equipement.
     *
     * @return Equipement
     */
    public Equipement getEquipement( )
    {
        return _equipement;
    }

    /**
     * Gets the date service fait traitement.
     *
     * @return the _strDateServiceFaitTraitement
     */
    public String getDateServiceFaitTraitement( )
    {
        return _strDateServiceFaitTraitement;
    }

    /**
     * Sets the date service fait traitement.
     *
     * @param strDateServiceFaitTraitement
     *            the new date service fait traitement
     */
    public void setDateServiceFaitTraitement( String strDateServiceFaitTraitement )
    {
        _strDateServiceFaitTraitement = strDateServiceFaitTraitement;
    }

    /**
     * Gets the heure service fait traitement.
     *
     * @return the _strHeureServiceFaitTraitement
     */
    public String getHeureServiceFaitTraitement( )
    {
        return _strHeureServiceFaitTraitement;
    }

    /**
     * Sets the heure service fait traitement.
     *
     * @param strHeureServiceFaitTraitement
     *            the new heure service fait traitement
     */
    public void setHeureServiceFaitTraitement( String strHeureServiceFaitTraitement )
    {
        _strHeureServiceFaitTraitement = strHeureServiceFaitTraitement;
    }

    /**
     * Getter for the _nFelicitation.
     *
     * @return the _nFelicitation
     */
    public int getFelicitations( )
    {
        return _nFelicitations;
    }

    /**
     * Setter for the _nFelicitation.
     *
     * @param nFelicitations
     *            the _nFelicitation to set
     */
    public void setFelicitations( int nFelicitations )
    {
        _nFelicitations = nFelicitations;
    }

    /**
     * Getter for the observationRejet List.
     *
     * @return List of obervsation rejet
     */
    public List<ObservationRejet> getObservationsRejet( )
    {
        return _observationsRejet;
    }

    /**
     * Setter for the observationRejet list.
     *
     * @param observationsRejet
     *            The observationRejet of this signalement
     */
    public void setObservationsRejet( List<ObservationRejet> observationsRejet )
    {
        _observationsRejet = observationsRejet;
    }

    /**
     * Getter for the id reference for web service.
     *
     * @return the id reference for web service
     */
    public String getSignalementReference( )
    {
        String reference;
        String prefix = getPrefix( );
        String annee = StringUtils.EMPTY + getAnnee( ) + StringUtils.EMPTY;
        String mois = getMois( );
        String numero = StringUtils.EMPTY + getNumero( ) + StringUtils.EMPTY;
        reference = prefix + annee + mois + numero;

        return reference;
    }

    /**
     * Gets the commentaire agent terrain.
     *
     * @return the commentaire agent terrain
     */
    public String getCommentaireAgentTerrain( )
    {
        return _strCommentaireAgentTerrain;
    }

    /**
     * Sets the commentaire agent terrain.
     *
     * @param strCommentaireAgentTerrain
     *            the new commentaire agent terrain
     */
    public void setCommentaireAgentTerrain( String strCommentaireAgentTerrain )
    {
        _strCommentaireAgentTerrain = strCommentaireAgentTerrain;
    }

    /**
     * Gets the nombre feedback.
     *
     * @return the nombre feedback
     */
    public int getNombreFeedback( )
    {
        return _nNombreFeedback;
    }

    /**
     * Sets the nombre feedback.
     *
     * @param nNombreFeedback
     *            the new nombre feedback
     */
    public void setNombreFeedback( int nNombreFeedback )
    {
        this._nNombreFeedback = nNombreFeedback;
    }
}
