<?php

namespace ${namespaceDir};

use Doctrine\ORM\EntityRepository;

/**
 <#if repoDeclared>
 * @ORM\Entity(repositoryClass="${namespaceForRepository}\${name}Repository")
<#else>
 * @ORM\Entity
</#if>
 * @ORM\Table(name="${optionalTableName}")
 */
class ${name}Repository extends EntityRepository
{
   
}