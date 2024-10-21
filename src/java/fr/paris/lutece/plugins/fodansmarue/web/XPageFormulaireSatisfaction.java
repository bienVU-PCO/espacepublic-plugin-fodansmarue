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
package fr.paris.lutece.plugins.fodansmarue.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;

/**
 * The Class XPageFormulaireSatisfaction.
 */
@Controller( xpageName = AbstractXPage.XPAGE_FORMULAIRE_SATISFACTION, pageTitleI18nKey = "fodansmarue.xpage.dansmarue.pageTitle", pagePathI18nKey = "fodansmarue.xpage.dansmarue.pageTitle" )
public class XPageFormulaireSatisfaction extends AbstractXPage
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TEMPLATE_XPAGE_SUIVI_SIGNALEMENT. */
    // TEMPLATES
    private static final String TEMPLATE_XPAGE_FORMULAIRE_SATISFACTION = "/skin/plugins/fosignalement/formulaire_satisfaction.html";

    /** The Constant ACTION_VALIDER_CHOIX. */
    private static final String ACTION_VALIDER_CHOIX = "valider_choix";

    /** The Constant MARK_MESSAGE_ERREUR. */
    private static final String MARK_MESSAGE_ERREUR = "erreur";

    /** The Constant MARK_SIGNALEMENT. */
    // MARKER
    private static final String MARK_SIGNALEMENT = "signalement";

    /** The Constant MARK_REPONSE_FORMULAIRE. */
    private static final String MARK_REPONSE_FORMULAIRE = "reponseFormulaire";

    /** The Constant MARK_COMMENTAIRE_FORMULAIRE. */
    private static final String MARK_COMMENTAIRE_FORMULAIRE = "commentaire";

    /** The Constant MARK_TOKEN. */
    private static final String MARK_TOKEN = "token";

    /** The Constant MARK_IS_SATISFACTION_FORM_COMPLETE. */
    private static final String MARK_IS_SATISFACTION_FORM_COMPLETE = "isSatisfactionFormComplete";

    /** The Constant PROPERTY_ID_STATE_SERVICE_FAIT. */
    // PROPERTIES
    public static final String PROPERTY_ID_STATE_SERVICE_FAIT = "signalement.idStateServiceFait";

    private String _strToken;

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @View( value = AbstractXPage.XPAGE_FORMULAIRE_SATISFACTION, defaultView = true )
    public XPage viewFormulaireSatisfaction( HttpServletRequest request ) throws IOException
    {
        Map<String, Object> model = getModel( );
        _strToken = request.getParameter( MARK_TOKEN );

        model.put( MARK_IS_SATISFACTION_FORM_COMPLETE, false );
        model.put( MARK_TOKEN, _strToken );
        return getXPage( TEMPLATE_XPAGE_FORMULAIRE_SATISFACTION, request.getLocale( ), model );
    }

    /**
     * Save the user's choice.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Action( ACTION_VALIDER_CHOIX )
    public XPage validerChoix( HttpServletRequest request ) throws JSONException, IOException
    {
        Map<String, Object> model = getModel( );

        String strReponseFormulaire = request.getParameter( MARK_REPONSE_FORMULAIRE );
        String strCommentaire = request.getParameter( MARK_COMMENTAIRE_FORMULAIRE );

        _signalementBoService.sauvegarderReponsesFormulaireSatisfaction( _strToken, strReponseFormulaire, strCommentaire );

        String error = "";

        if ( StringUtils.isEmpty( strReponseFormulaire ) )
        {
            model.put( MARK_MESSAGE_ERREUR, error );
        }

        model.put( MARK_IS_SATISFACTION_FORM_COMPLETE, true );

        return getXPage( TEMPLATE_XPAGE_FORMULAIRE_SATISFACTION, request.getLocale( ), model );
    }

}
