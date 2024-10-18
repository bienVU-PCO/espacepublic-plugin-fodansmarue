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
package fr.paris.lutece.plugins.fodansmarue.utils.constants;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * SignalementConstants.
 */
public final class SignalementConstants
{

    /** The Constant ROLE_OBSERVATEUR_MAIRIES. */
    // ROLES
    public static final String ROLE_OBSERVATEUR_MAIRIES = "OBSERVATEUR_MAIRIES";

    /** The Constant VALUE_PRIORITE_PEU_GENANT_ID. */
    // VALUES
    public static final Integer VALUE_PRIORITE_PEU_GENANT_ID = 3;

    /** The Constant FIELD_LAT. */
    // FIELDS
    public static final String FIELD_LAT = "lat";

    /** The Constant FIELD_LNG. */
    public static final String FIELD_LNG = "lng";

    /** The Constant SIGNALEMENT_WORKFLOW_ID. */
    public static final Integer SIGNALEMENT_WORKFLOW_ID = 2;

    /** The Constant MARK_JSP_BACK. */
    // MARKS
    public static final String MARK_JSP_BACK = "jsp_back";

    /** The Constant MARK_NB_ITEMS_PER_PAGE. */
    public static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    /** The Constant MARK_PAGINATOR. */
    public static final String MARK_PAGINATOR = "paginator";

    /** The Constant MARK_FILTER. */
    public static final String MARK_FILTER = "filter";

    /** The Constant MARK_LOCALE. */
    public static final String MARK_LOCALE = "locale";

    /** The Constant PROPERTY_DEFAULT_ITEM_PER_PAGE. */
    // Properties
    public static final String PROPERTY_DEFAULT_ITEM_PER_PAGE = "signalement.itemsPerPage";

    /** The Constant PARAMETER_BUTTON_CANCEL. */
    // Parameters
    public static final String PARAMETER_BUTTON_CANCEL = "cancel";

    /** The Constant PARAMETER_ERROR. */
    public static final String PARAMETER_ERROR = "error";

    /** The Constant PARAMETER_VALIDATE_NEXT. */
    public static final String PARAMETER_VALIDATE_NEXT = "validate_next";

    /** The Constant PARAMETER_BUTTON_SEARCH. */
    public static final String PARAMETER_BUTTON_SEARCH = "search";

    /** The Constant PARAMETER_PRIORITE. */
    public static final String PARAMETER_PRIORITE = "priorite";

    /** The Constant MESSAGE_ERROR_OCCUR. */
    // Messages
    public static final String MESSAGE_ERROR_OCCUR = "dansmarue.message.error.erroroccur";

    /** The Constant MESSAGE_SIGNALEMENT_NOT_FOUND. */
    public static final String MESSAGE_SIGNALEMENT_NOT_FOUND = "dansmarue.task_annulation_signalement.signalementNotFound";

    /** The Constant MESSAGE_OBSERVATION_NOT_FOUND. */
    public static final String MESSAGE_OBSERVATION_NOT_FOUND = "task_annulation_signalement.observationNotFound";

    /** The Constant ATTRIBUTE_HAS_NEXT. */
    // Attribute
    public static final String ATTRIBUTE_HAS_NEXT = "has_next";

    /** The Constant ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION. */
    // Attribute session
    public static final String ATTRIBUTE_SESSION_DERNIERE_SAISIE_ACTION = "derniere_saisie_action";

    /** The Constant ATTRIBUTE_SESSION_IS_USER_RECTRICTED. */
    public static final String ATTRIBUTE_SESSION_IS_USER_RECTRICTED = "signalement_is_user_restricted";

    /** The Constant ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS. */
    public static final String ATTRIBUTE_SESSION_LIST_RESTRICTED_ARRONDISSEMENTS = "signalement_list_restricted_arrondissements";

    /** The Constant ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT. */
    public static final String ATTRIBUTE_SESSION_LIST_RESTRICTED_TYPE_SIGNALEMENT = "signalement_list_restricted_type_signalement";

    /** The Constant ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT. */
    public static final String ATTRIBUTE_SESSION_LIST_RESTRICTED_CATEGORY_SIGNALEMENT = "signalement_list_restricted_category_signalement";

    /** The Constant REFERENCE_LIST_ID. */
    // pour les lists
    public static final String REFERENCE_LIST_ID = "id";

    /** The Constant REFERENCE_LIST_LIBELLE. */
    public static final String REFERENCE_LIST_LIBELLE = "libelle";

    /** The Constant GOOGLE_MAPS_API_KEY. */
    // google maps
    public static final String GOOGLE_MAPS_API_KEY = AppPropertiesService.getProperty( "signalement.maps.key" );

    /** The Constant ARCHIVE_LIMIT. */
    // achive
    public static final String ARCHIVE_LIMIT = "signalement.archive.limit";

    /** The Constant IMAGE_RESIZE_WIDTH. */
    // images resizing
    public static final String IMAGE_RESIZE_WIDTH = "image.resize.width";

    /** The Constant IMAGE_RESIZE_HEIGHT. */
    public static final String IMAGE_RESIZE_HEIGHT = "image.resize.height";

    /** The Constant IMAGE_THUMBNAIL_RESIZE_WIDTH. */
    public static final String IMAGE_THUMBNAIL_RESIZE_WIDTH = "imageThumbnail.resize.width";

    /** The Constant IMAGE_THUMBNAIL_RESIZE_HEIGHT. */
    public static final String IMAGE_THUMBNAIL_RESIZE_HEIGHT = "imageThumbnail.resize.height";

    /** The Constant UNIT_ATELIER_JARDINAGE. */
    // units property keys definitions
    public static final String UNIT_ATELIER_JARDINAGE = "signalement.unit.atelierJardinage";

    /** The Constant UNIT_JARDINAGE. */
    public static final String UNIT_JARDINAGE = "signalement.unit.jardinage";

    /** The Constant UNIT_SYLVICOLE. */
    public static final String UNIT_SYLVICOLE = "signalement.unit.sylvicole";

    /** The Constant UNIT_CIMETIERE. */
    public static final String UNIT_CIMETIERE = "signalement.unit.cimetiere";

    /** The Constant UNIT_DEVE. */
    public static final String UNIT_DEVE = AppPropertiesService.getProperty( "signalement.unit.deve" );

    /** The Constant UNIT_DPE. */
    public static final Integer UNIT_DPE = AppPropertiesService.getPropertyInt( "signalement.unit.dpe", -1 );

    /** The Constant TYPE_SIGNALEMENT_ENCOMBRANT. */
    // type signalement keys definitions
    public static final String TYPE_SIGNALEMENT_ENCOMBRANT = "signalement.typeSignalement.encombrant";

    /** The Constant INC_VERSION_TYPE_SIGNALEMENT. */
    // inc version type signalement
    public static final String INC_VERSION_TYPE_SIGNALEMENT = "signalement.inc.typeSignalement";

    /** The Constant SIGNALEMENT_PREFIX_IOS. */
    // Préfixes des signalements
    public static final String SIGNALEMENT_PREFIX_IOS = AppPropertiesService.getProperty( "signalement.prefix.origin.ios" );

    /** The Constant SIGNALEMENT_PREFIX_ANDROID. */
    public static final String SIGNALEMENT_PREFIX_ANDROID = AppPropertiesService.getProperty( "signalement.prefix.origin.android" );

    /** The Constant SIGNALEMENT_PREFIX_TELESERVICE. */
    public static final String SIGNALEMENT_PREFIX_TELESERVICE = AppPropertiesService.getProperty( "signalement.prefix.origin.teleservice" );

    /** The Constant SIGNALEMENT_PREFIX_BACKOFFICE. */
    public static final String SIGNALEMENT_PREFIX_BACKOFFICE = AppPropertiesService.getProperty( "signalement.prefix.origin.backoffice" );

    /** The Constant SIGNALEMENT_PREFIX_KEY. */
    public static final String SIGNALEMENT_PREFIX_KEY = "signalement.prefix.origin";

    /** The Constant PROPERTY_REST_SIGNALEMENT_BO_URL. */
    // Methodes WS
    public static final String PROPERTY_REST_SIGNALEMENT_BO_URL = "signalement-bo-rest-url";

    /** The Constant PROPERTY_REST_EQUIPEMENT_BO_URL. */
    public static final String PROPERTY_REST_EQUIPEMENT_BO_URL = "equipement-bo-rest-url";

    /** The Constant PROPERTY_SIGNALEMENT. */
    public static final String PROPERTY_SIGNALEMENT = "signalement";

    /** The Constant PROPERTY_EQUIPEMENT. */
    public static final String PROPERTY_EQUIPEMENT = "equipement";

    /** The Constant PROPERTY_ESPACE_PUBLIC_VALUE. */
    // Value type d'anomalie déclarée
    public static final String PROPERTY_ESPACE_PUBLIC_VALUE = "espace-public-value";

    /** The Constant PROPERTY_URL_STORE_ADR. */
    public static final String PROPERTY_URL_STORE_ADR = "signalement.storeAdr.url";

    /** The Constant REST_GET_GEOM_FROM_LAMBER_TO_WS_84. */
    public static final String REST_GET_GEOM_FROM_LAMBER_TO_WS_84 = "getGeomFromLambertToWgs84";

    /** The Constant REST_GET_GEOM_FROM_LAMBERT_93_TO_WS_84. */
    public static final String REST_GET_GEOM_FROM_LAMBERT_93_TO_WS_84 = "getGeomFromLambert93ToWgs84";

    /** The Constant REST_FIND_ALL_SIGNALEMENT_IN_PERIMETER_WITH_DTO. */
    public static final String REST_FIND_ALL_SIGNALEMENT_IN_PERIMETER_WITH_DTO = "findAllSignalementInPerimeterWithDTO";

    /** The Constant REST_GET_DISTANCE_BETWEEN_SIGNALEMENT. */
    public static final String REST_GET_DISTANCE_BETWEEN_SIGNALEMENT = "getDistanceBetweenSignalement";

    /** The Constant REST_IS_SIGNALEMENT_FOLLOWABLE. */
    public static final String REST_IS_SIGNALEMENT_FOLLOWABLE = "isSignalementFollowable";

    /** The Constant REST_GET_ALL_PRIORITE. */
    public static final String REST_GET_ALL_PRIORITE = "getAllPriorite";

    /** The Constant REST_LOAD_PRIORITE_BY_ID. */
    public static final String REST_LOAD_PRIORITE_BY_ID = "loadPrioriteById";

    /** The Constant REST_GET_ARRONDISSEMENT_BY_GEOM. */
    public static final String REST_GET_ARRONDISSEMENT_BY_GEOM = "getArrondissementByGeom";

    /** The Constant REST_ADD_FOLLOWER. */
    public static final String REST_ADD_FOLLOWER = "addFollower";

    /** The Constant REST_GET_TYPE_SIGNALEMENT_TREE. */
    public static final String REST_GET_TYPE_SIGNALEMENT_TREE = "getTypeSignalementTree";

    /** The Constant REST_GET_TYPE_SIGNALEMENT_TREE_FOR_SOURCE. */
    public static final String REST_GET_TYPE_SIGNALEMENT_TREE_FOR_SOURCE = "getTypeSignalementTreeForSource/";

    /** The Constant REST_GET_INFO_FOR_SOURCE. */
    public static final String REST_GET_INFO_FOR_SOURCE = "getInfosForSource/";

    /** The Constant REST_GET_TYPE_SIGNALEMENT. */
    public static final String REST_GET_TYPE_SIGNALEMENT = "getTypeSignalement";

    /** The Constant REST_FIND_BY_ID_TYPE_SIGNALEMENT. */
    public static final String REST_FIND_BY_ID_TYPE_SIGNALEMENT = "findByIdTypeSignalement";

    /** The Constant GET_ADRESSE_ITEM. */
    public static final String GET_ADRESSE_ITEM = "getAddressItem";

    /** The Constant GET_DOSSIERS_COURRANTS_BY_GEOM_WITH_LIMIT. */
    public static final String GET_DOSSIERS_COURRANTS_BY_GEOM_WITH_LIMIT = "getDossiersCourrantsByGeomWithLimit";

    /** The Constant SAVE_SIGNALEMENT. */
    public static final String SAVE_SIGNALEMENT = "sauvegarderSignalement";

    /** The Constant SAVE_SATISFACTION_FORM_ANSWER. */
    public static final String SAVE_SATISFACTION_FORM_ANSWER = "saveSatisfactionFormAnswer";

    /** The Constant REST_GET_EQUIPEMENTS. */
    public static final String REST_GET_EQUIPEMENTS = "getEquipements";

    /** The Constant REST_GET_INCIDENT_BY_EQUIPEMENT. */
    public static final String REST_GET_INCIDENT_BY_EQUIPEMENT = "getIncidentsByEquipement";

    /** The Constant REST_GET_EQUIPEMENT_BY_POSITION. */
    public static final String REST_GET_EQUIPEMENT_BY_POSITION = "getEquipementsByPosition";

    /** The Constant REST_GET_SIGNALEMENT_BY_TOKEN. */
    public static final String REST_GET_SIGNALEMENT_BY_TOKEN = "getSignalementByToken";

    /** The Constant REST_GET_HISTORY_SIGNALEMENT. */
    public static final String REST_GET_HISTORY_SIGNALEMENT = "getHistorySignalement";

    /** The Constant REST_SERVICE_FAIT_SIGNALEMENT. */
    public static final String REST_SERVICE_FAIT_SIGNALEMENT = "validateServiceFaitSignalement";

    /** The Constant RETOUR_CREATION_SIGNALEMENT. */
    public static final String RETOUR_CREATION_SIGNALEMENT = "isSaveSignalementOk";

    /** The Constant RETOUR_SAVE_SATISFACTION_FORM_ANSWER. */
    public static final String RETOUR_SAVE_SATISFACTION_FORM_ANSWER = "isSaveSatisfactionFormAnswer";

    /** The Constant CODE_ERREUR_CREATION_SIGNALEMENT. */
    public static final String CODE_ERREUR_CREATION_SIGNALEMENT = "codeErreurCreation";

    /** The Constant ERREUR_SAUVEGARDE_SIGNALEMENT. */
    public static final Integer ERREUR_SAUVEGARDE_SIGNALEMENT = 0;

    /** The Constant ERREUR_SAUVEGARDE_PHOTO. */
    public static final Integer ERREUR_SAUVEGARDE_PHOTO = 1;

    /** State Workflow Echec envoie WS. */
    public static final Integer STATE_FAILURE_SEND_WS = 20;

    /**
     * Sepcial Address
     */
    public static final String BRIDGE_ADDRESS = "pont";

    /**
     * Utility class - empty constructor.
     */
    private SignalementConstants( )
    {
        // nothing
    }

}
